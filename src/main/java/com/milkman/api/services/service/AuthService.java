package com.milkman.api.services.service;

import com.milkman.api.dto.AuthRequest;
import com.milkman.api.dto.AuthResponse;

/**
 * @Author: kchid
 * @Project: pom.xml
 * @Date: 8/26/2023
 * @Time: 12:28 PM
 */
public interface AuthService {
    AuthResponse login(AuthRequest requestBuilder);
}
