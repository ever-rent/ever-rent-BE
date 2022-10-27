package com.finalproject.everrent_be.domain.oauth.model;


import com.finalproject.everrent_be.domain.token.dto.TokenDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OauthResponseModel {

    private Integer code;
    private HttpStatus httpStatus;
    private String message;
    private List<?> data;
    private TokenDto tokenDto;
}