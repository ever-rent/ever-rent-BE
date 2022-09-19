package com.finalproject.everrent_be.dto;

import com.finalproject.everrent_be.model.Member;
import com.finalproject.everrent_be.model.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponseDto {

    private Long id;

    private String productName;
    private String price;
    private String content;
    private String imgUrl;

    private LocalDateTime writeAt;

    public ProductResponseDto(Product product)
    {
        this.id=product.getId();
        this.productName=product.getProductName();
        this.content=product.getContent();
        this.price=product.getPrice();
        this.imgUrl=product.getImgUrl();
        this.writeAt=product.getModifiedAt();
    }

}
