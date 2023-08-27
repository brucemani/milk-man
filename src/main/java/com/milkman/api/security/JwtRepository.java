package com.milkman.api.security;

import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.google.gson.Gson;
import com.milkman.api.dto.AuthRequest;
import com.milkman.api.dto.AuthResponse;
import com.milkman.api.dto.TokenInfo;
import com.milkman.api.model.Customer;
import com.milkman.api.model.Role;
import com.milkman.api.services.service.CustomerService;
import com.milkman.api.services.service.RoleService;
import com.milkman.api.util.enums.Privilege;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;

import static com.auth0.jwt.JWT.create;
import static com.auth0.jwt.JWT.require;
import static com.auth0.jwt.algorithms.Algorithm.HMAC256;
import static com.milkman.api.util.common.CommonUtil.BEARER;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.time.LocalDateTime.now;
import static java.util.Calendar.HOUR;
import static java.util.Calendar.getInstance;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toSet;
import static org.springframework.security.crypto.bcrypt.BCrypt.checkpw;

@Component
@Slf4j
public class JwtRepository {
    @Value("${app.jwt.secret-key}")
    private String secret;
    @Value("${app.jwt.expire}")
    private Integer jwtExpireHours;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private RoleService roleService;

    private Date getAccessTokenExpire() {
        final Calendar calendar = getInstance();
        calendar.setTime(new Date());
        calendar.add(HOUR, this.jwtExpireHours);
        return calendar.getTime();
    }

    private Date getRefreshTokenExpire() {
        final Calendar calendar = getInstance();
        calendar.setTime(new Date());
        calendar.add(HOUR, this.jwtExpireHours + 1);
        return calendar.getTime();
    }

    private final Function<String, TokenInfo> isValidToken = (bearerToke) -> {
        if (bearerToke == null || bearerToke.isEmpty()) {
            log.error("Jwt token missing!");
            throw new BadCredentialsException("Jwt token missing!");
        }
        if (!bearerToke.startsWith(BEARER)) {
            log.error("Bearer key word missing in the token!");
            throw new BadCredentialsException("Bearer key word missing in the token!");
        }
        final String token = bearerToke.substring(BEARER.length());
        final Algorithm algorithm = HMAC256(secret.getBytes(UTF_8));
        final JWTVerifier verifier = require(algorithm).build();
        final TokenInfo tokenInfo = new Gson().fromJson(verifier.verify(token).getSubject(), TokenInfo.class);
        final UserDetails userDetails = customUserDetailsService.loadUserByUsername(tokenInfo.getUserName());
        if (userDetails.getUsername() != null && !userDetails.getUsername().isEmpty() && userDetails.getUsername().equalsIgnoreCase(tokenInfo.getUserName())) {
            tokenInfo.setUserDetails(userDetails);
            return tokenInfo;
        }
        log.error("Invalid user access!");
        throw new RuntimeException("Invalid user access!");
    };
    public final Function<String, TokenInfo> extractTokenInfo = (token) -> {
        if (token == null || token.isEmpty()) {
            log.error("Authorization token missing!");
            throw new NullPointerException("Authorization token missing!");
        }
        return this.isValidToken.apply(token);
    };


    public final Function<AuthRequest, AuthResponse> generateAccessToken = (req) -> {
        final Customer customer = this.customerService.findCustomerByEmail(req.getUserName()).orElseGet(Customer::new);
        final Role role = this.roleService.findRoleByCustomerId(customer.getCustomerId()).orElseGet(Role::new);
        if (customer.getCustomerEmail() == null || customer.getCustomerEmail().isEmpty()) {
            log.error("Incorrect user name!");
            throw new BadCredentialsException("Incorrect user name!");
        }
        if (!checkpw(req.getPassword(), customer.getCustomerPassword())) {
            log.error("Incorrect password!");
            throw new BadCredentialsException("Incorrect password!");
        }
        final Set<String> roles = role.getRoleList().stream().filter(Objects::nonNull).collect(toSet());
        final TokenInfo userInfo = TokenInfo.builder().provider(req.getRequest().getServletPath()).userName(customer.getCustomerEmail()).roles(roles).build();
        final Algorithm algorithm = HMAC256(secret);
        final Date access_token_expire = getAccessTokenExpire();
        final Date refresh_token_expire = getRefreshTokenExpire();
        final List<String> privilegeList = role.getPrivilegeList().stream().map(Privilege::name).toList();
        final String access_token = create()
                .withSubject(new Gson().toJson(userInfo))
                .withIssuedAt(new Date())
                .withExpiresAt(access_token_expire)
                .withIssuer(req.getRequest().getRequestURL().toString())
                .withClaim("roles", new ArrayList<>(roles)).sign(algorithm);
        final String refresh_token = create()
                .withSubject(new Gson().toJson(userInfo))
                .withIssuedAt(new Date())
                .withExpiresAt(refresh_token_expire)
                .withIssuer(req.getRequest().getRequestURL().toString())
                .withClaim("roles", new ArrayList<>(roles)).sign(algorithm);
        return AuthResponse.builder().userId(customer.getCustomerId()).role(new ArrayList<>(roles)).privilege(privilegeList).userName(customer.getCustomerEmail()).message("Authenticate successful").accessToken(BEARER.concat(access_token)).refreshToken(BEARER.concat(refresh_token)).createAt(now().toString()).expireDate(access_token_expire.toString()).build();
    };

    public final BiFunction<AuthResponse, HttpServletRequest, AuthResponse> refreshToken = (auth, req) -> {
        final TokenInfo tokenInfo = ofNullable(this.extractTokenInfo.apply(auth.getRefreshToken())).orElseThrow(() -> new NullPointerException("Got exception while extract jwt token!"));
        final Customer customer = this.customerService.findCustomerByEmail(tokenInfo.getUserName()).orElseGet(Customer::new);
        final Role role = this.roleService.findRoleByCustomerId(customer.getCustomerId()).orElseGet(Role::new);
        final Set<String> roles = role.getRoleList().stream().filter(Objects::nonNull).collect(toSet());
        final TokenInfo userInfo = TokenInfo.builder().provider(req.getServletPath()).userName(customer.getCustomerEmail()).roles(roles).build();
        final Algorithm algorithm = HMAC256(secret);
        final Date access_token_expire = getAccessTokenExpire();
        final Date refresh_token_expire = getRefreshTokenExpire();
        final List<String> privilegeList = role.getPrivilegeList().stream().map(Privilege::name).toList();
        final String access_token = create()
                .withSubject(new Gson().toJson(userInfo))
                .withIssuedAt(new Date())
                .withExpiresAt(access_token_expire)
                .withIssuer(req.getRequestURL().toString())
                .withClaim("roles", new ArrayList<>(roles)).sign(algorithm);
        final String refresh_token = create()
                .withSubject(new Gson().toJson(userInfo))
                .withIssuedAt(new Date())
                .withExpiresAt(refresh_token_expire)
                .withIssuer(req.getRequestURL().toString())
                .withClaim("roles", new ArrayList<>(roles)).sign(algorithm);
        return AuthResponse.builder().userId(customer.getCustomerId()).role(new ArrayList<>(roles)).privilege(privilegeList).userName(customer.getCustomerEmail()).message("Authenticate successful").accessToken(BEARER.concat(access_token)).refreshToken(BEARER.concat(refresh_token)).createAt(now().toString()).expireDate(access_token_expire.toString()).build();
    };
}
