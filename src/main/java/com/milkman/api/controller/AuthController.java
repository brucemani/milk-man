package com.milkman.api.controller;

import com.milkman.api.dto.AuthRequest;
import com.milkman.api.dto.AuthResponse;
import com.milkman.api.dto.ResponseBuilder;
import com.milkman.api.model.Customer;
import com.milkman.api.services.service.AuthService;
import com.milkman.api.services.service.CustomerService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import static com.milkman.api.util.enums.ResponseHandler.makeResponse;
import static com.milkman.api.util.enums.Status.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.ResponseEntity.ok;

/**
 * @Author: kchid
 * @Project: pom.xml
 * @Date: 8/26/2023
 * @Time: 12:26 PM
 */
@RestController
@RequestMapping(path = "/api/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final CustomerService customerService;

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBuilder> authenticate(@RequestParam @NonNull final String username, @NonNull final String password, final HttpServletRequest request) {
        final AuthResponse authResponse = authService.login(AuthRequest.builder().userName(username).password(password).request(request).build());
        return ok(makeResponse(authResponse, SUCCESS.getStatus(), SUCCESS.getMessage()));
    }

    @PostMapping(produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBuilder> register(@RequestBody @Valid @NonNull final Customer customer) {
        return ok(makeResponse(customerService.save(customer), CREATED.getStatus(), CREATED.getMessage()));
    }

    @PutMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBuilder> refreshToken(@RequestBody @Valid @NonNull final AuthResponse req, final HttpServletRequest request) {
        return ok(makeResponse(authService.refreshToken(req, request), UPDATE.getStatus(), UPDATE.getMessage()));
    }

//    @PostMapping(path = "/forgot",consumes = APPLICATION_JSON_VALUE,produces = APPLICATION_JSON_VALUE)
//    public ResponseEntity<ResponseBuilder> forgotPassword(@RequestBody @Valid @NonNull PasswordRequest request){
//
//    }

}
