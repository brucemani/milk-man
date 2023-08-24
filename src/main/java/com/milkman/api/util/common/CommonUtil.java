package com.milkman.api.util.common;

import com.milkman.api.dto.UrlRequestBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.milkman.api.util.enums.DateFormatPatterns.LOCAL_DATE;
import static java.time.ZoneId.systemDefault;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;
import static java.util.UUID.randomUUID;
import static java.util.stream.LongStream.range;

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
    public static final String BEARER="Bearer ";
    @Value("${app.baseUrl}")
    private String BASE;

    @Value("${app.otpLen}")
    private Integer otpLen;

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

    public final Function<String, Date> utilDateConvertor = date -> {
        try {
            return new SimpleDateFormat(LOCAL_DATE.getPattern()).parse(date);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            ex.printStackTrace();
            throw new RuntimeException(ex.getMessage());
        }
    };

    public final Function<Date, LocalDate> dateToLocalDate= date -> date.toInstant().atZone(systemDefault()).toLocalDate();


}
