package com.finalproject.everrent_be.domain.product.dto;

import com.finalproject.everrent_be.domain.product.model.Product;
import com.finalproject.everrent_be.global.common.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponseDto {

    private Long id;

    private String memberName;
    private String productName;
    private String price;
    private String content;
    private String imgUrl;
    private String cateId;
    private String rentStart;
    private String  rentEnd;
    private String location;
    private Status status;

    private LocalDateTime writeAt;

    private String[] imgUrlArray;



    public ProductResponseDto(Product product)
    {
        this.id=product.getId();
        this.memberName=product.getMember().getMemberName();
        this.productName=product.getProductName();
        this.price=product.getPrice();
        this.content=product.getContent();
        this.imgUrlArray=StringUrlToArray(product.getImgUrl());
        this.cateId=product.getCateId();
        this.writeAt=product.getModifiedAt();
        this.rentStart=LocalDateToStr(product.getRentStart());
        this.rentEnd=LocalDateToStr(product.getRentEnd());
        this.location=product.getLocation();
        this.status=product.getStatus();
    }

    public String[] StringUrlToArray(String s){
        imgUrlArray=s.split(" ");
        return imgUrlArray;
    }

    public String LocalDateToStr(LocalDate localDate){
        return localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }



}
