package com.milkman.api.ExceptionHandler;

import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.IncorrectClaimException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.milkman.api.dto.ResponseBuilder;
import jakarta.servlet.ServletException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;

import static com.milkman.api.util.enums.ResponseHandler.makeResponse;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.ResponseEntity.status;

/**
 * @Author: kchid
 * @Project: milky-man
 * @Date: 7/30/2023
 * @Time: 1:07 PM
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<ResponseBuilder> runtimeException(RuntimeException exception) {
        exception.printStackTrace();
        return status(INTERNAL_SERVER_ERROR).body(makeResponse(null, INTERNAL_SERVER_ERROR.value(), exception.getMessage(), exception.getMessage()));
    }

    @ExceptionHandler({NullPointerException.class})
    public ResponseEntity<ResponseBuilder> nullPointerException(NullPointerException exception) {
        return status(BAD_REQUEST).body(makeResponse(null, BAD_REQUEST.value(), exception.getMessage(), exception.getMessage()));
    }

    @ExceptionHandler({BadCredentialsException.class})
    public ResponseEntity<ResponseBuilder> badCredential(BadCredentialsException exception) {
        return status(UNAUTHORIZED).body(makeResponse(null, UNAUTHORIZED.value(), exception.getMessage(), exception.getMessage()));
    }

    @ExceptionHandler({UsernameNotFoundException.class})
    public ResponseEntity<ResponseBuilder> userNotFoundException(UsernameNotFoundException exception) {
        return status(UNAUTHORIZED).body(makeResponse(null, UNAUTHORIZED.value(), exception.getMessage(), exception.getMessage()));
    }

    @ExceptionHandler({ServletException.class})
    public ResponseEntity<ResponseBuilder> servletException(ServletException exception) {
        return status(UNAUTHORIZED).body(makeResponse(null, UNAUTHORIZED.value(), exception.getMessage(), exception.getMessage()));
    }

    @ExceptionHandler({IOException.class})
    public ResponseEntity<ResponseBuilder> iOException(IOException exception) {
        return status(UNAUTHORIZED).body(makeResponse(null, UNAUTHORIZED.value(), exception.getMessage(), exception.getMessage()));
    }

    @ExceptionHandler({AlgorithmMismatchException.class})
    public ResponseEntity<ResponseBuilder> algorithmMismatch(AlgorithmMismatchException exception) {
        return status(FORBIDDEN).body(makeResponse(null, FORBIDDEN.value(), exception.getMessage(), exception.getMessage()));
    }

    @ExceptionHandler({SignatureVerificationException.class})
    public ResponseEntity<ResponseBuilder> signatureMismatch(SignatureVerificationException exception) {
        return status(FORBIDDEN).body(makeResponse(null, FORBIDDEN.value(), exception.getMessage(), exception.getMessage()));
    }

    @ExceptionHandler({TokenExpiredException.class})
    public ResponseEntity<ResponseBuilder> tokenExpiry(TokenExpiredException exception) {
        return status(FORBIDDEN).body(makeResponse(null, FORBIDDEN.value(), exception.getMessage(), exception.getMessage()));
    }

    @ExceptionHandler({IncorrectClaimException.class})
    public ResponseEntity<ResponseBuilder> incorrectClaim(IncorrectClaimException exception) {
        return status(FORBIDDEN).body(makeResponse(null, FORBIDDEN.value(), exception.getMessage(), exception.getMessage()));
    }
    @ExceptionHandler({AccessDeniedException.class, InternalAuthenticationServiceException.class})
    public ResponseEntity<ResponseBuilder> accessDenied(Exception exception) {
        return status(FORBIDDEN).body(makeResponse(null, FORBIDDEN.value(), exception.getMessage(), exception.getMessage()));
    }
}
