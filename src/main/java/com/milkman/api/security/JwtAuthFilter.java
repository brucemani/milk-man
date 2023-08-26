package com.milkman.api.security;

import com.google.gson.Gson;
import com.milkman.api.dto.TokenInfo;
import com.milkman.api.util.enums.Status;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


import java.io.IOException;
import java.util.*;

import static com.milkman.api.util.common.CommonUtil.BEARER;
import static com.milkman.api.util.enums.ResponseHandler.makeResponse;
import static java.util.Collections.singletonList;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Component
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtRepository jwtRepository;

    @Value("#{'${app.auth.urls}'.split(',')}")
    private Set<String> unAuthUrls;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws IOException {
        try {
            final String token = request.getHeader(AUTHORIZATION);
            if (token == null || !token.startsWith(BEARER)) {
                log.error("Invalid jwt token!");
                throw new RuntimeException("Invalid jwt token!");
            }
            final TokenInfo tokenInfo = jwtRepository.extractTokenInfo.apply(token);
            final UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(tokenInfo.getUserDetails(), null, tokenInfo.getUserDetails().getAuthorities());
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            filterChain.doFilter(request, response);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            response.setStatus(UNAUTHORIZED.value());
            response.setContentType(APPLICATION_JSON_VALUE);
            response.getWriter().print(new Gson().toJson(makeResponse(new Object(), UNAUTHORIZED.value(), ex.getMessage())));
        }
    }

    private boolean isNotFilterUrl(final String url) {
        if (url == null || url.isEmpty()) {
            log.error("URL should not be empty or null!");
            throw new NullPointerException("URL should not be empty or null!");
        }
        return this.unAuthUrls.stream().filter(Objects::nonNull).anyMatch(url::startsWith);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return isNotFilterUrl(request.getServletPath());
    }
}
