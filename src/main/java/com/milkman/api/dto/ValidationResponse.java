package com.milkman.api.dto;

import lombok.Builder;
import lombok.Data;

/**
 * @Author: kchid
 * @Project: milky-man
 * @Date: 8/2/2023
 * @Time: 9:58 PM
 */
@Data
@Builder
public class ValidationResponse {
    private String message;
    private Integer status;
    private String error;
    private String timeStamp;
}
