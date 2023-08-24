package com.milkman.api.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class AuthResponse {
    private String userName;
    private String accessToken;
    private String expireDate;
    private String createAt;
    private String message;
}
