package com.milkman.api.services.service;

import com.milkman.api.dto.AuthRequest;
import com.milkman.api.dto.AuthResponse;
import com.milkman.api.dto.PasswordRequest;
import com.milkman.api.dto.PasswordResponse;
import jakarta.servlet.http.HttpServletRequest;

/**
 * @Author: kchid
 * @Project: pom.xml
 * @Date: 8/26/2023
 * @Time: 12:28 PM
 */
public interface AuthService {
    AuthResponse login(AuthRequest requestBuilder);
    AuthResponse refreshToken(AuthResponse req, HttpServletRequest httpServletRequest);
    PasswordResponse forgotPassword(PasswordRequest request);
    PasswordResponse resetPassword(PasswordRequest request);
}
