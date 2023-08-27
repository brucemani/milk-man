package com.milkman.api.util.enums;

import org.springframework.http.HttpStatus;

/**
 * @Author: kchid
 * @Project: milky-man
 * @Date: 7/30/2023
 * @Time: 1:06 PM
 */
public enum Status {
    SUCCESS("Query executed successful.", HttpStatus.OK.value()),
    CREATED("Record created.", HttpStatus.CREATED.value()),
    FOUND("Record found.", HttpStatus.FOUND.value()),
    NOT_FOUND("Record not found.", HttpStatus.NOT_FOUND.value()),
    UPDATE("Record updated.", HttpStatus.ACCEPTED.value()),
    DELETE("Record deleted.", HttpStatus.ACCEPTED.value()),
    ACCESS_DENIED("Access denied.", HttpStatus.FORBIDDEN.value()),
    VALIDATION_SUCCESS("Validation success.", HttpStatus.OK.value());

    private final String message;
    private final Integer status;

    Status(String message, Integer status) {
        this.message = message;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public Integer getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "Status{" +
                "message='" + message + '\'' +
                ", status=" + status +
                '}';
    }
}
