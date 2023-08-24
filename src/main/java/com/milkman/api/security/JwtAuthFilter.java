package com.milkman.api.security;

import com.milkman.api.dto.TokenInfo;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


import java.io.IOException;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static com.milkman.api.util.common.CommonUtil.BEARER;
import static java.util.Collections.singletonList;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
@Slf4j
@AllArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtRepository jwtRepository;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {

        final String token = request.getHeader(AUTHORIZATION);
        if (token == null || !token.startsWith(BEARER)) {
            filterChain.doFilter(request, response);
            log.error("Invalid jwt token!");
            throw new ServletException("Invalid jwt token!");
        }
        final TokenInfo tokenInfo = jwtRepository.extractTokenInfo.apply(token);
        final UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(tokenInfo.getUserDetails(), null, tokenInfo.getUserDetails().getAuthorities());
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);
    }

    private static boolean isNotFilterUrl(final String url) {
        final Set<String> notFilterUrl = new HashSet<>(singletonList("/api/validation"));
        if (url == null || url.isEmpty()) {
            log.error("URL should not be empty or null!");
            throw new NullPointerException("URL should not be empty or null!");
        }
        return notFilterUrl.stream().filter(Objects::nonNull).anyMatch(url::startsWith);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return isNotFilterUrl(request.getServletPath());
    }
}
