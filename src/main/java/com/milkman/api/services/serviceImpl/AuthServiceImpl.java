package com.milkman.api.services.serviceImpl;

import com.milkman.api.dto.*;
import com.milkman.api.model.Customer;
import com.milkman.api.security.JwtRepository;
import com.milkman.api.services.service.ApplicationValidationService;
import com.milkman.api.services.service.AuthService;
import com.milkman.api.services.service.CustomerService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static com.milkman.api.util.enums.Status.VALIDATION_SUCCESS;

/**
 * @Author: kchid
 * @Project: pom.xml
 * @Date: 8/26/2023
 * @Time: 12:35 PM
 */
@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class AuthServiceImpl implements AuthService {

    private final CustomerService customerService;
    private final AuthenticationManager authenticationManager;
    private final JwtRepository jwtRepository;
    private final ApplicationValidationService validationService;

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

    @Override
    public PasswordResponse forgotPassword(@NonNull PasswordRequest request) {
        final ValidationResponse validation = this.validationService.tokenValidation(request.getToken());
        if (Objects.equals(validation.getStatus(), VALIDATION_SUCCESS.getStatus())) {
            this.customerService.updatePassword(validation.getUserId(), request.getNewPassword());
            log.info("Your password has been changed successful!");
            return PasswordResponse.builder().message("Your password has been changed successful.").userId(validation.getUserId()).build();
        }
        throw new RuntimeException("Forgot password failed!");
    }

    @Override
    public PasswordResponse resetPassword(@NonNull PasswordRequest request) {
        this.customerService.updatePassword(request.getUserId(), request.getNewPassword());
        return PasswordResponse.builder().message("Your password successful reset!").userId(request.getUserId()).build();
    }
}
