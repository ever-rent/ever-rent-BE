package com.finalproject.everrent_be.domain.wishlist.dto;

import com.finalproject.everrent_be.domain.wishlist.model.WishList;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WishListResponseDto {
    private Long wishListId;
    private Long productId;
    private String memberName;
    private String productName;
    private String price;
    private String[] imgUrlArray;

    private LocalDateTime productWriteAt;

    public WishListResponseDto(WishList wishList)
    {
        this.wishListId= wishList.getId();
        this.productId=wishList.getProduct().getId();
        this.memberName=wishList.getMember().getMemberName();
        this.productName=wishList.getProduct().getProductName();
        this.price=wishList.getProduct().getPrice();
        this.imgUrlArray=StringUrlToArray(wishList.getProduct().getImgUrl());
        this.productWriteAt=wishList.getProduct().getModifiedAt();
    }
    public String[] StringUrlToArray(String s){
        imgUrlArray=s.split(" ");
        return imgUrlArray;
    }

}
