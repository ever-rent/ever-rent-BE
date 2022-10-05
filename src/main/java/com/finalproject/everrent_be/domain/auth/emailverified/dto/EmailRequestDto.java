package com.finalproject.everrent_be.domain.auth.emailverified.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EmailRequestDto {
    private String email;

}
