package com.milkman.api.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

import static com.milkman.api.util.enums.Status.ACCESS_DENIED;

/**
 * @Author: kchid
 * @Project: pom.xml
 * @Date: 8/27/2023
 * @Time: 3:14 PM
 */
@Slf4j
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        log.info("I am executed");
        response.sendError(ACCESS_DENIED.getStatus(), ACCESS_DENIED.toString());
    }
}
