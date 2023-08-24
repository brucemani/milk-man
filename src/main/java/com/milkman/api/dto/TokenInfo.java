package com.milkman.api.dto;

import lombok.*;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class TokenInfo {
    private String userName;
    private Set<String> roles;
    private String provider;
    private UserDetails userDetails;
}
