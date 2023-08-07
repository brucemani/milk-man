package com.milkman.api.controller;

import com.milkman.api.dto.ResponseBuilder;
import com.milkman.api.dto.ValidationRequestBuilder;
import com.milkman.api.services.service.ApplicationValidationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.milkman.api.util.enums.ResponseHandler.makeResponse;
import static com.milkman.api.util.enums.Status.SUCCESS;
import static com.milkman.api.util.enums.Status.VALIDATION_SUCCESS;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.ResponseEntity.ok;

/**
 * @Author: kchid
 * @Project: milky-man
 * @Date: 8/5/2023
 * @Time: 9:42 PM
 */
@RestController
@RequestMapping(path = "/api/validation")
@AllArgsConstructor
public class ValidationController {

    private final ApplicationValidationService validationService;
    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBuilder> verifyOtp(@RequestBody ValidationRequestBuilder request) {
        return ok(makeResponse(validationService.otpValidation(request), VALIDATION_SUCCESS.getStatus(), VALIDATION_SUCCESS.getMessage()));
    }

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBuilder> verifyToken(@RequestParam String token) {
        return ok(makeResponse(validationService.tokenValidation(token), VALIDATION_SUCCESS.getStatus(), VALIDATION_SUCCESS.getMessage()));
    }

    @PostMapping(path = "/testOtp",consumes = APPLICATION_JSON_VALUE,produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBuilder> testOtp(@RequestBody ValidationRequestBuilder req){
        this.validationService.sendOtp(req.getEmail(),req.getMobileNumber());
        return ok(makeResponse(null, SUCCESS.getStatus() ,SUCCESS.getMessage()));
    }

    @PostMapping(path = "/testToken",consumes = APPLICATION_JSON_VALUE,produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBuilder> testToken(@RequestBody ValidationRequestBuilder req){
        this.validationService.sendValidationLink(req.getEmail());
        return ok(makeResponse(null, SUCCESS.getStatus(),SUCCESS.getMessage()));
    }

}
