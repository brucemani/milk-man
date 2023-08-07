package com.milkman.api.dto;

import lombok.*;

/**
 * @Author: kchid
 * @Project: milky-man
 * @Date: 8/5/2023
 * @Time: 10:15 PM
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ValidationRequestBuilder {
    private Long otp;
    private String mobileNumber;
    private String email;
    private String token;
}
