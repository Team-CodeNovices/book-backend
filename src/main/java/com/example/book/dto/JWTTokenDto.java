package com.example.book.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class JWTTokenDto {
    private String grantType;
    private String accessToken;
    private String refreshToken;
}
