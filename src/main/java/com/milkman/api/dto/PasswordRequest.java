package com.milkman.api.dto;

import jakarta.validation.constraints.NotNull;
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
public class PasswordRequest {
    @NotNull(message = "UserId shouldn't null!")
    private Long userId;
    @NotNull(message = "")
    private String currentPassword;
    private String newPassword;
    private String confirmPassword;
    private String token;
}
