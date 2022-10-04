package com.finalproject.everrent_be.dto;

import com.finalproject.everrent_be.model.WishList;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WishListResponseDto {
    private Long id;
    private String productName;
    private String price;
    private String[] imgUrlArray;

    public WishListResponseDto(WishList wishList)
    {
        this.id= wishList.getId();
        this.productName=wishList.getProduct().getProductName();
        this.price=wishList.getProduct().getPrice();
        this.imgUrlArray=StringUrlToArray(wishList.getProduct().getImgUrl());
    }
    public String[] StringUrlToArray(String s){
        imgUrlArray=s.split(" ");
        return imgUrlArray;
    }

}
