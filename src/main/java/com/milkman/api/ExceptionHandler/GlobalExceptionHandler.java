package com.milkman.api.ExceptionHandler;

import com.milkman.api.dto.ResponseBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.milkman.api.util.enums.ResponseHandler.makeResponse;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
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
        return status(INTERNAL_SERVER_ERROR).body(makeResponse(null, INTERNAL_SERVER_ERROR.value(), exception.getMessage(), exception.getMessage()));
    }

    @ExceptionHandler({NullPointerException.class})
    public ResponseEntity<ResponseBuilder> nullPointerException(NullPointerException exception) {
        return status(BAD_REQUEST).body(makeResponse(null, BAD_REQUEST.value(), exception.getMessage(), exception.getMessage()));
    }
}
