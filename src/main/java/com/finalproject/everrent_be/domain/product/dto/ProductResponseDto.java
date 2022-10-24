package com.finalproject.everrent_be.domain.product.dto;

import com.finalproject.everrent_be.domain.member.model.Member;
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
    private Long memberId;
    private String badges;
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
    //private int wishNum;
    private String rating;
    private LocalDateTime writeAt;

    private String[] imgUrlArray;

    private boolean heart;




    public ProductResponseDto(Product product)
    {
        this.id=product.getId();
        this.memberId=product.getMember().getId();
        this.badges=product.getMember().getBadges();
        this.memberName=product.getMember().getMemberName();
        this.productName=product.getProductName();
        this.price=Integer.parseInt(product.getPrice());
        this.content=product.getContent();
        this.imgUrlArray=StringUrlToArray(product.getImgUrl());
        this.cateId=product.getCateId();
        this.writeAt=product.getModifiedAt();
        this.rating=product.getMember().getRating();
        this.location=product.getLocation();
        this.mapLocation=product.getMapLocation();
        this.rentStart=LocalDateToStr(product.getRentStart());
        this.rentEnd=LocalDateToStr(product.getRentEnd());
        this.location=product.getLocation();
        this.status=product.getStatus();
        this.heart=false;
        //this.wishNum=product.getWishNum();
    }

    public String[] StringUrlToArray(String s){
        imgUrlArray=s.split(" ");
        return imgUrlArray;
    }

    public String LocalDateToStr(LocalDate localDate){
        return localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
    public void UpdateLike(Boolean heart){
        this.heart=heart;
    }


}
