package com.milkman.api.services.service;

import com.milkman.api.dto.ValidationRequestBuilder;
import com.milkman.api.dto.ValidationResponse;
import com.milkman.api.model.ApplicationValidation;

import java.util.Optional;

/**
 * @Author: kchid
 * @Project: milky-man
 * @Date: 8/2/2023
 * @Time: 9:22 PM
 */
public interface ApplicationValidationService extends CommonService<ApplicationValidation, Long> {
    Optional<ApplicationValidation> findByMobileNumberAndOtp(String mobile, Long otp);

    Optional<ApplicationValidation> findByToken(String token);

    ValidationResponse otpValidation(ValidationRequestBuilder request);

    ValidationResponse tokenValidation(String token);

    Long generateOtp(String mobileNumber);

    String generateToken();

    ValidationResponse otpVerification(ValidationRequestBuilder request);

    ValidationResponse tokenVerification(ValidationRequestBuilder request);

    void sendValidationLink(String email);

    void sendOtp(String email,String mobile);
}
