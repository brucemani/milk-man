package com.milkman.api.services.serviceImpl;

import com.milkman.api.dto.*;
import com.milkman.api.mail.MailSenderService;
import com.milkman.api.model.ApplicationValidation;
import com.milkman.api.model.Customer;
import com.milkman.api.repository.ApplicationValidationRepository;
import com.milkman.api.services.service.ApplicationValidationService;
import com.milkman.api.services.service.CustomerService;
import com.milkman.api.util.common.CommonUtil;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static com.milkman.api.util.enums.DateFormatPatterns.QUERY_DATE_TIME;
import static com.milkman.api.util.enums.DateFormatPatterns.RESPONSE_DATE_PATTERN;
import static com.milkman.api.util.enums.MailTemplate.ACTIVATE_REQUEST;
import static com.milkman.api.util.enums.MailTemplate.OTP;
import static com.milkman.api.util.enums.Status.SUCCESS;
import static com.milkman.api.util.enums.Status.VALIDATION_SUCCESS;
import static com.milkman.api.util.enums.UrlTemplate.ACTIVATE_ACCOUNT;
import static java.lang.String.valueOf;
import static java.time.LocalDateTime.now;
import static java.time.LocalDateTime.parse;
import static java.time.format.DateTimeFormatter.ofPattern;
import static java.util.Objects.requireNonNull;

/**
 * @Author: kchid
 * @Project: milky-man
 * @Date: 8/2/2023
 * @Time: 9:23 PM
 */
@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class ApplicationValidationServiceImpl implements ApplicationValidationService {

    private final ApplicationValidationRepository repository;

    private final MailSenderService mailSenderService;

    private final CommonUtil commonUtil;

    private final CustomerService customerService;

    @Override
    public ApplicationValidation save(@NonNull ApplicationValidation obj) {
        return repository.save(obj);
    }

    @Override
    public Optional<ApplicationValidation> findById(@NonNull Long id) {
        return repository.findById(id);
    }

    @Override
    public List<ApplicationValidation> findAll() {
        return repository.findAll();
    }

    @Override
    public ApplicationValidation updateById(@NonNull ApplicationValidation obj) {
        requireNonNull(obj.getId(), "Primary key shouldn't be null.");
        this.findById(obj.getId()).orElseThrow(() -> new NullPointerException("Given id not exist in DB."));
        return repository.save(obj);
    }

    @Override
    public void deleteById(@NonNull Long id) {
        this.repository.deleteById(id);
    }

    @Override
    public Optional<ApplicationValidation> findByMobileNumberAndOtp(@NonNull String mobile, @NonNull Long otp) {
        return this.repository.findByMobileNumberAndOtp(mobile, otp);
    }

    @Override
    public Optional<ApplicationValidation> findByToken(@NonNull String token) {
        return this.repository.findByToken(token);
    }

    @Override
    public ValidationResponse otpValidation(@NonNull ValidationRequestBuilder request) {
        final ApplicationValidation result = this.findByMobileNumberAndOtp(request.getMobileNumber(), request.getOtp()).orElseThrow(() -> new NullPointerException("Invalid mobile number or otp."));
        if (!(now().isBefore(parse(result.getExpireTime(), ofPattern(QUERY_DATE_TIME.getPattern()))))) {
            log.error("OTP expired.");
            throw new RuntimeException("OTP expired.");
        }
        if (result.getIsVerified() != null && result.getIsVerified()) {
            log.error("OTP already verified.");
            throw new RuntimeException("OTP already verified.");
        }
        result.setIsVerified(true);
        this.save(result);
        return ValidationResponse
                .builder()
                .message(VALIDATION_SUCCESS.getMessage())
                .status(VALIDATION_SUCCESS.getStatus())
                .error(null)
                .timeStamp(ZonedDateTime.now().format(ofPattern(RESPONSE_DATE_PATTERN.getPattern())))
                .build();
    }

    @Override
    public ValidationResponse tokenValidation(@NonNull String token) {
        final ApplicationValidation result = this.findByToken(token).orElseThrow(() -> new NullPointerException("Invalid token."));
        if (!(now().isBefore(parse(result.getExpireTime(), ofPattern(QUERY_DATE_TIME.getPattern()))))) {
            log.error("Token expired.");
            throw new RuntimeException("Token expired.");
        }
        if (result.getIsVerified() != null && result.getIsVerified()) {
            log.error("Token already verified.");
            throw new RuntimeException("Token already verified.");
        }
        result.setIsVerified(true);
        this.save(result);
        final Customer customer = this.customerService.findCustomerByEmail(result.getIdentity()).orElseThrow(() -> new NullPointerException("Invalid Identity!"));
        return ValidationResponse
                .builder()
                .userId(customer.getCustomerId())
                .message(VALIDATION_SUCCESS.getMessage())
                .status(VALIDATION_SUCCESS.getStatus())
                .error(null)
                .timeStamp(now().format(ofPattern(RESPONSE_DATE_PATTERN.getPattern())))
                .build();
    }

    @Override
    public Long generateOtp(@NonNull String mobileNumber) {
        Long otp = commonUtil.generateOtp.get();
        final ApplicationValidation result = this.findByMobileNumberAndOtp(mobileNumber, otp).orElseGet(ApplicationValidation::new);
        if (result.getId() != null) {
            otp = this.generateOtp(mobileNumber);
        }
        final ApplicationValidation validation = ApplicationValidation
                .builder()
                .isVerified(false)
                .expireTime(now().plusMinutes(this.commonUtil.otpExpire).format(ofPattern(QUERY_DATE_TIME.getPattern())))
                .otp(otp)
                .mobileNumber(mobileNumber)
                .build();
        this.save(validation);
        return otp;
    }

    @Override
    public String generateToken() {
        String token = commonUtil.generateToken.get();
        final ApplicationValidation result = this.findByToken(token).orElseGet(ApplicationValidation::new);
        if (result.getId() != null) {
            log.warn("This token already exist so execute the token rotation!");
            token = this.generateToken();
        }
        final ApplicationValidation validation = ApplicationValidation
                .builder()
                .token(token)
                .expireTime(now().plusMinutes(this.commonUtil.otpExpire).format(ofPattern(QUERY_DATE_TIME.getPattern())))
                .isVerified(false)
                .build();
        this.save(validation);
        return token;
    }

    @Override
    public ValidationResponse otpVerification(@NonNull ValidationRequestBuilder request) {
        if (request.getMobileNumber() == null || request.getMobileNumber().isEmpty()) {
            log.error("Mobile number shouldn't be null or empty.");
            throw new NullPointerException("Mobile number shouldn't be null or empty.");
        }
        if (request.getOtp() == null) {
            log.error("OTP shouldn't be null.");
            throw new NullPointerException("OTP shouldn't be null.");
        }
        final ApplicationValidation result = this.findByMobileNumberAndOtp(request.getMobileNumber(), request.getOtp()).orElseThrow(() -> new NullPointerException("OTP is not match given mobile number."));
        if (result.getIsVerified()) {
            log.error("OTP already verified.");
            throw new RuntimeException("OTP already verified.");
        }

        if (parse(result.getExpireTime(), ofPattern(QUERY_DATE_TIME.getPattern())).isBefore(now())) {
            result.setIsVerified(true);
            this.save(result);
            log.info("OTP verification successful.");
            return ValidationResponse
                    .builder()
                    .timeStamp(now().format(ofPattern(QUERY_DATE_TIME.getPattern())))
                    .error(null)
                    .status(SUCCESS.getStatus())
                    .message("OTP validation successful.")
                    .build();
        }
        log.error("OTP expired.");
        throw new RuntimeException("OTP expired.");
    }

    @Override
    public ValidationResponse tokenVerification(@NonNull ValidationRequestBuilder request) {
        if (request.getToken() == null || request.getToken().isEmpty()) {
            log.error("Token shouldn't be null or empty.");
            throw new NullPointerException("Token shouldn't be null or empty.");
        }
        final ApplicationValidation result = this.findByToken(request.getToken()).orElseThrow(() -> new NullPointerException("Invalid token."));
        if (result.getIsVerified()) {
            log.error("Token already verified.");
            throw new RuntimeException("Token already verified.");
        }
        if (parse(result.getExpireTime(), ofPattern(QUERY_DATE_TIME.getPattern())).isBefore(now())) {
            result.setIsVerified(true);
            this.save(result);
            log.info("Token verification successful.");
            return ValidationResponse
                    .builder()
                    .timeStamp(now().format(ofPattern(QUERY_DATE_TIME.getPattern())))
                    .error(null)
                    .status(SUCCESS.getStatus())
                    .message("Token validation successful.")
                    .build();
        }
        log.error("Token expired.");
        throw new RuntimeException("Token expired.");
    }

    @Override
    public void sendValidationLink(@NonNull String email) {
        final String token = this.generateToken();
        final UrlRequestBuilder urlRequestBuilder = UrlRequestBuilder
                .builder()
                .params(Map.of("token", token))
                .template(ACTIVATE_ACCOUNT)
                .build();
        final String url = this.commonUtil.urlBuilder.apply(urlRequestBuilder);
        final MailSenderRequestBuilder build = MailSenderRequestBuilder
                .builder()
                .to(email)
                .subject("Account activate request")
                .params(Map.of("username", email, "url", url))
                .template(ACTIVATE_REQUEST)
                .build();
        final MailSenderResponseBuilder response = mailSenderService.sendMail(build);
        if (!Objects.equals(response.getStatus(), SUCCESS.getStatus())) {
            log.error("Email sending failed.");
            throw new RuntimeException("Email sending failed.");
        }
    }

    @Override
    public void sendOtp(@NonNull String email, @NonNull String mobile) {
        final Long otp = this.generateOtp(mobile);
        final MailSenderRequestBuilder mailRequest = MailSenderRequestBuilder
                .builder()
                .to(email)
                .subject("App verification code")
                .template(OTP)
                .params(Map.of("username", email, "otp", valueOf(otp)))
                .build();
        final MailSenderResponseBuilder response = mailSenderService.sendMail(mailRequest);
        if (!Objects.equals(response.getStatus(), SUCCESS.getStatus())) {
            log.error("Email sending failed.");
            throw new RuntimeException("Email sending failed.");
        }
    }
}
