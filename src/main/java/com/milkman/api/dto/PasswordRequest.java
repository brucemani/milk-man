package com.milkman.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
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
    @NotNull(message = "Current password shouldn't null")
    @NotEmpty(message = "Current password shouldn't empty!")
    @NotBlank(message = "Current password shouldn't blank!")
    private String currentPassword;
    @NotNull(message = "New password shouldn't null")
    @NotEmpty(message = "New password shouldn't empty!")
    @NotBlank(message = "New password shouldn't blank!")
    private String newPassword;
    @NotNull(message = "Confirm password shouldn't null")
    @NotEmpty(message = "Confirm password shouldn't empty!")
    @NotBlank(message = "Confirm password shouldn't blank!")
    private String confirmPassword;
    @NotNull(message = "Token shouldn't null")
    @NotEmpty(message = "Token shouldn't empty!")
    @NotBlank(message = "Token shouldn't blank!")
    private String token;
}
