package com.milkman.api.security;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.util.Set;

import static org.springframework.security.config.Customizer.withDefaults;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

/**
 * @Author: kchid
 * @Project: pom.xml
 * @Date: 8/24/2023
 * @Time: 11:31 PM
 */
@Configuration
@AllArgsConstructor
@EnableWebSecurity
@EnableMethodSecurity
public class AppSecurity {

    @Autowired
    private AuthenticationProvider authenticationProvider;

    @Autowired
    private JwtRepository jwtRepository;

    @Autowired
    private HandlerExceptionResolver handlerExceptionResolver;


    @Value("#{'${app.auth.urls}'.split(',')}")
    private final Set<String> unAuthUrls;


    @Bean
    public JwtAuthFilter jwtAuthFilter() {
        return new JwtAuthFilter(handlerExceptionResolver, jwtRepository, unAuthUrls);
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req -> req.requestMatchers("/api/validation/**", "/api/auth/**").permitAll().anyRequest().authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .authenticationProvider(this.authenticationProvider)
                .exceptionHandling(customize -> customize.accessDeniedHandler(this.accessDeniedHandler()))
                .addFilterBefore(jwtAuthFilter(), UsernamePasswordAuthenticationFilter.class)
                .httpBasic(withDefaults())
                .logout(logout -> logout.clearAuthentication(true).logoutUrl("/api/auth/logout").invalidateHttpSession(true))
                .build();
    }
}
