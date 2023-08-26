package com.milkman.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: kchid
 * @Project: pom.xml
 * @Date: 8/26/2023
 * @Time: 11:05 PM
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PasswordResponse {
    private Long userId;
    private String message;
}
