package com.finalproject.everrent_be.domain.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductRequestDto {

    private String productName;
    private String price;
    private String content;
    private String cateId;
    private String rentStart;
    private String rentEnd;
    private String location;

}
