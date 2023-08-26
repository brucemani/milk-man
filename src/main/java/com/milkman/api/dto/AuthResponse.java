package com.milkman.api.dto;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class AuthResponse {
    private Long userId;
    private String userName;
    private String accessToken;
    private String refreshToken;
    private List<String> role;
    private List<String> privilege;
    private String expireDate;
    private String createAt;
    private String message;
}
