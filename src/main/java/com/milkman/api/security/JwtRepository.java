package com.milkman.api.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.*;
import com.google.gson.Gson;

import com.milkman.api.dto.AuthRequest;
import com.milkman.api.dto.AuthResponse;
import com.milkman.api.dto.TokenInfo;
import com.milkman.api.model.Customer;
import com.milkman.api.model.Role;
import com.milkman.api.services.service.CustomerService;
import com.milkman.api.services.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

import static com.auth0.jwt.JWT.require;
import static com.auth0.jwt.algorithms.Algorithm.HMAC256;
import static com.milkman.api.util.common.CommonUtil.BEARER;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.time.LocalDateTime.now;
import static java.util.Calendar.MINUTE;
import static java.util.Calendar.getInstance;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

@Component
@Slf4j
public class JwtRepository {
    @Value("${app.jwt.secret-key}")
    private String secret;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private RoleService roleService;

    private static Date getAccessTokenExpire() {
        final Calendar calendar = getInstance();
        calendar.setTime(new Date());
        calendar.add(MINUTE, 1);
        return calendar.getTime();
    }

    private final Function<String, TokenInfo> isValidToken = (bearerToke) -> {
        try {
            if (bearerToke == null || bearerToke.isEmpty()) {
                log.error("Jwt token missing!");
                throw new NullPointerException("Jwt token missing!");
            }
            if (!bearerToke.startsWith(BEARER)) {
                log.error("Bearer key word missing in the token!");
                throw new NullPointerException("Bearer key word missing in the token!");
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
        } catch (AlgorithmMismatchException | SignatureVerificationException | TokenExpiredException |
                 MissingClaimException | IncorrectClaimException ex) {
            log.error(ex.getLocalizedMessage());
            throw new RuntimeException(ex.getLocalizedMessage());
        }
    };
    public final Function<String, TokenInfo> extractTokenInfo = (token) -> {
        try {
            if (token == null || token.isEmpty()) {
                log.error("Authorization token missing!");
                throw new NullPointerException("Authorization token missing!");
            }
            return this.isValidToken.apply(token);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new RuntimeException(ex.getMessage());
        }
    };


    public final Function<AuthRequest, AuthResponse> generateAccessToken = (req) -> {
        try {
            final Customer customer = this.customerService.findCustomerByEmail(req.getUserName()).orElseGet(Customer::new);
            final Role role = this.roleService.findRoleByCustomerId(customer.getCustomerId()).orElseGet(Role::new);
            if (customer.getCustomerEmail() == null || customer.getCustomerEmail().isEmpty()) {
                log.error("Incorrect user name!");
                throw new RuntimeException("Incorrect user name!");
            }
            if (!BCrypt.checkpw(req.getPassword(), customer.getCustomerPassword())) {
                log.error("Incorrect password!");
                throw new RuntimeException("Incorrect password!");
            }
            final Set<String> roles = role.getRoleList().stream().filter(Objects::nonNull).collect(toSet());
            final TokenInfo userInfo = TokenInfo.builder().provider(req.getRequest().getServletPath()).userName(customer.getCustomerEmail()).roles(roles).build();
            final Algorithm algorithm = HMAC256(secret);
            final Date expire = getAccessTokenExpire();
            String access_token = JWT.create()
                    .withSubject(new Gson().toJson(userInfo))
                    .withIssuedAt(new Date())
                    .withExpiresAt(expire)
                    .withIssuer(req.getRequest().getRequestURL().toString())
                    .withClaim("roles", role.getRoleList().parallelStream().filter(Objects::nonNull).collect(toList())).sign(algorithm);
            return AuthResponse.builder().userName(req.getUserName()).message("Authenticate successful").accessToken(BEARER.concat(access_token)).createAt(now().toString()).expireDate(expire.toString()).build();
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error(ex.getMessage());
            throw new RuntimeException(ex.getMessage());
        }
    };


}
