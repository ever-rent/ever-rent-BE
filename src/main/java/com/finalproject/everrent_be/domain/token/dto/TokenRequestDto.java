package com.finalproject.everrent_be.domain.token.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenRequestDto {
    private String accessToken;
    private String refreshToken;
}