package com.milkman.api.util.enums;

import com.milkman.api.dto.ResponseBuilder;

import static com.milkman.api.util.enums.DateFormatPatterns.RESPONSE_DATE_PATTERN;
import static java.time.ZonedDateTime.now;
import static java.time.format.DateTimeFormatter.ofPattern;

/**
 * @Author: kchid
 * @Project: milky-man
 * @Date: 7/30/2023
 * @Time: 1:08 PM
 */
public class ResponseHandler {
    public static ResponseBuilder makeResponse(Object obj, int status, String message, Object error) {
        return ResponseBuilder
                .builder()
                .payload(obj)
                .status(status)
                .message(message)
                .timeStamp(now().format(ofPattern(RESPONSE_DATE_PATTERN.getPattern())))
                .error(error)
                .build();
    }

    public static ResponseBuilder makeResponse(Object obj, int status, String message) {
        return ResponseBuilder
                .builder()
                .payload(obj)
                .status(status)
                .message(message)
                .timeStamp(now().format(ofPattern(RESPONSE_DATE_PATTERN.getPattern())))
                .error(null)
                .build();
    }
}
