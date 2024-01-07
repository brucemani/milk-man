package com.milkman.api.util.common;

import com.milkman.api.dto.PasswordRequest;
import com.milkman.api.dto.UrlRequestBuilder;
import com.milkman.api.model.Customer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.regex.Pattern;

import static com.milkman.api.util.enums.DateFormatPatterns.LOCAL_DATE;
import static java.lang.Character.*;
import static java.time.LocalDate.parse;
import static java.time.ZoneId.systemDefault;
import static java.time.format.DateTimeFormatter.ofPattern;
import static java.util.Base64.getEncoder;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;
import static java.util.UUID.randomUUID;
import static java.util.regex.Pattern.compile;
import static java.util.stream.LongStream.range;
import static org.springframework.security.crypto.bcrypt.BCrypt.checkpw;

/**
 * @Author: kchid
 * @Project: milky-man
 * @Date: 8/2/2023
 * @Time: 9:38 PM
 */
@Component
@Slf4j
public class CommonUtil {
    public static final String MOBILE_REGEX = "^(?:(?:\\+|0{0,2})91(\\s*[\\-]\\s*)?|[0]?)?[789]\\d{9}$";
    public static final String OTP_REGEX = "(\\d{6})";
    public static final String BEARER = "Bearer ";
    @Value("${app.baseUrl}")
    private String BASE;

    @Value("${app.otpLen}")
    private Integer otpLen;
    private static final String PASSWORD_STRENGTH_REGEX = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}";

    @Value("${app.otpExpire}")
    public Integer otpExpire;

    public final Function<UrlRequestBuilder, String> urlBuilder = req -> {
        requireNonNull(req, "Param shouldn't be null.");
        if (this.BASE == null || this.BASE.isEmpty()) {
            log.error("Base URL shouldn't be null or empty.");
            throw new NullPointerException("Base URL shouldn't be null or empty.");
        }
        final Map<String, String> params = ofNullable(req.getParams()).orElseGet(HashMap::new);
        final String[] baseUrl = {BASE.concat(req.getTemplate().getTemplate())};
        params.entrySet()
                .stream()
                .filter(Objects::nonNull)
                .filter(f -> f.getKey() != null && f.getValue() != null)
                .forEachOrdered(obj -> baseUrl[0] = baseUrl[0].concat(obj.getKey()).concat("=").concat(obj.getValue()).concat("&"));
        return baseUrl[0].endsWith("&") ? baseUrl[0].substring(0, baseUrl[0].length() - 1) : baseUrl[0];
    };
    public final Supplier<Long> generateOtp = () -> {
        if (otpLen == null || otpLen < 4) {
            log.warn("Otp default length applied because otp length should be greater than or equal >=4.");
            otpLen = 4;
        }
        if (otpLen >= 19) {
            log.error("Otp length param should be less than or equal to 18.");
            throw new RuntimeException("Otp length param should be less than or equal to >=18.");
        }
        final Long from = range(1, otpLen - 1).reduce(10, (v1, v2) -> v1 * 10);
        final Long to = range(1, otpLen).reduce(10, (v1, v2) -> v1 * 10);
        return new Random().nextLong(from, to - 1);
    };

    public final Supplier<String> generateToken = () -> randomUUID().toString().replace("-", "");

    public final Function<String, LocalDate> utilDateConvertor = date -> {
        try {
            return parse(date, ofPattern(LOCAL_DATE.getPattern()));
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new RuntimeException(ex.getMessage());
        }
    };

    public final Function<Date, LocalDate> dateToLocalDate = date -> date.toInstant().atZone(systemDefault()).toLocalDate();


    private final Predicate<String> passwordStrengthChecker = pass -> compile(PASSWORD_STRENGTH_REGEX).matcher(pass).matches();


    public final Function<byte[], String> imageToBase64 = arr -> getEncoder().encodeToString(arr);

    public final BiConsumer<PasswordRequest, Customer> validatePassword = (req, cus) -> {
        if (!req.getNewPassword().equalsIgnoreCase(req.getConfirmPassword())) {
            log.error("New password and current password is not match!");
            throw new RuntimeException("New password and current password is not match!");
        }
        if (checkpw(req.getCurrentPassword(), cus.getCustomerPassword())) {
            log.error("Current password shouldn't occur last password!");
            throw new RuntimeException("Current password shouldn't occur last password!");
        }
    };
}
