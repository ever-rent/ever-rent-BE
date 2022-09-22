package com.finalproject.everrent_be.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponseDto {

    private Long id;
    private String memberName;
    private String productName;
    private String buyStart;
    private String buyEnd;
    private String confirm;

}
