package com.milkman.api.security;

import com.milkman.api.dto.TokenInfo;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.Objects;
import java.util.Set;

import static com.milkman.api.util.common.CommonUtil.BEARER;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtRepository jwtRepository;
    private final HandlerExceptionResolver exceptionResolver;
    private final Set<String> unAuthUrls;

    public JwtAuthFilter(HandlerExceptionResolver exceptionResolver, JwtRepository jwtRepository, Set<String> urls) {
        this.exceptionResolver = exceptionResolver;
        this.jwtRepository = jwtRepository;
        this.unAuthUrls = urls;
    }

    //    @Value("#{'${app.auth.urls}'.split(',')}")
//    private final Set<String> unAuthUrls;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            final String token = request.getHeader(AUTHORIZATION);
            if (token == null || !token.startsWith(BEARER)) {
                log.error("Invalid jwt token!");
                throw new AccessDeniedException("Invalid jwt token!");
            }
            final TokenInfo tokenInfo = jwtRepository.extractTokenInfo.apply(token);
            final UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(tokenInfo.getUserDetails(), null, tokenInfo.getUserDetails().getAuthorities());
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            filterChain.doFilter(request, response);
        } catch (Exception ex) {
            exceptionResolver.resolveException(request, response, null, ex);
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
