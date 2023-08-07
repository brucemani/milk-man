package com.milkman.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: kchid
 * @Project: milky-man
 * @Date: 7/30/2023
 * @Time: 1:13 PM
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseBuilder {
    private Object payload;
    private Object status;
    private Object message;
    private Object error;
    private String timeStamp;
}
