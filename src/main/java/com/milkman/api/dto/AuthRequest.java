package com.milkman.api.dto;

import jakarta.servlet.http.HttpServletRequest;
import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class AuthRequest {
    private String userName;
    private String password;
    private HttpServletRequest request;
}
