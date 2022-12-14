package com.finalproject.everrent_be.domain.search.dto;

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
public class SearchResponseDto {

    private Long id;

    private Long memberId;
    private String memberName;
    private String productName;
    private int price;
    private String content;
    private String cateId;
    private String rentStart;
    private String rentEnd;
    private String location;
    private String mapLocation;
    private Status status;
    private int wishNum;
    private LocalDateTime writeAt;
    private String thumbimgUrl;
    private boolean heart;

    public SearchResponseDto(Product product,boolean heart)
    {
        this.id=product.getId();
        this.memberId=product.getMember().getId();
        this.memberName=product.getMember().getMemberName();
        this.productName=product.getProductName();
        this.price=Integer.parseInt(product.getPrice());
        this.content=product.getContent();
        this.cateId=product.getCateId();
        this.writeAt=product.getModifiedAt();
        this.location=product.getLocation();
        this.thumbimgUrl=GetThumnail(product.getImgUrl());
        this.mapLocation=product.getMapLocation();
        this.rentStart=LocalDateToStr(product.getRentStart());
        this.rentEnd=LocalDateToStr(product.getRentEnd());
        this.location=product.getLocation();
        this.status=product.getStatus();
        this.heart=heart;
        this.wishNum=product.getWishNum();
    }

    private String GetThumnail(String s){
        String[] imgUrlArray=s.split(" ");
        return imgUrlArray[0];
    }
    public String LocalDateToStr(LocalDate localDate){
        return localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
    public void UpdateLike(Boolean heart){
        this.heart=heart;
    }


}