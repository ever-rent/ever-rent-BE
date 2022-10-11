package com.finalproject.everrent_be.domain.product.dto;

import com.finalproject.everrent_be.domain.product.model.Product;
import com.finalproject.everrent_be.global.common.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductMainResponseDto {
    private Long id;
    //private String memberName;
    private String productName;
    private String price;
    //private String content;
    private String imgUrl;
    private String cateId;
    //private String rentStart;
    //private String  rentEnd;
    private String location;
    private String mapLocation;
    private Status status;
    private int wishNum;
    private LocalDateTime writeAt;
    private String[] imgUrlArray;
    private boolean isLike=false;

    public ProductMainResponseDto(Product product)
    {
        this.id=product.getId();
        //this.memberName=product.getMember().getMemberName();
        this.productName=product.getProductName();
        this.price=product.getPrice();
        //this.content=product.getContent();
        //this.imgUrlArray=StringUrlToArray(product.getImgUrl());
        this.cateId=product.getCateId();
        this.writeAt=product.getModifiedAt();
        this.location=product.getLocation();
        this.mapLocation=product.getMapLocation();
        //this.rentStart=LocalDateToStr(product.getRentStart());
        //this.rentEnd=LocalDateToStr(product.getRentEnd());
        this.location=product.getLocation();
        this.status=product.getStatus();
        this.wishNum=product.getWishNum();
    }


}
