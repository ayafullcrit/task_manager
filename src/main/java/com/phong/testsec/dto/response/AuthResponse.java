package com.phong.testsec.dto.response;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AuthResponse {
    private String token;
}