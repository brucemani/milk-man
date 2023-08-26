package com.milkman.api.services.serviceImpl;

import com.milkman.api.dto.AuthRequest;
import com.milkman.api.dto.AuthResponse;
import com.milkman.api.model.Customer;
import com.milkman.api.security.JwtRepository;
import com.milkman.api.services.service.AuthService;
import com.milkman.api.services.service.CustomerService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @Author: kchid
 * @Project: pom.xml
 * @Date: 8/26/2023
 * @Time: 12:35 PM
 */
@Service
@AllArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final CustomerService customerService;
    private final AuthenticationManager authenticationManager;
    private final JwtRepository jwtRepository;

    @Override
    public AuthResponse login(@NonNull AuthRequest authRequest) {
        final Customer customer = this.customerService.findCustomerByEmail(authRequest.getUserName()).orElseThrow(() -> new UsernameNotFoundException("User doesn't exist!"));
        if (customer.getCustomerId() != null) {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUserName(), authRequest.getPassword()));
            return jwtRepository.generateAccessToken.apply(authRequest);
        }
        throw new UsernameNotFoundException("Invalid user!");
    }

    @Override
    public AuthResponse refreshToken(@NonNull AuthResponse req, @NonNull HttpServletRequest request) {
        return this.jwtRepository.refreshToken.apply(req, request);
    }
}
