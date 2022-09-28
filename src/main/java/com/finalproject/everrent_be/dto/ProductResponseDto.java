package com.finalproject.everrent_be.dto;

import com.finalproject.everrent_be.model.Member;
import com.finalproject.everrent_be.model.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

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
    private String rentEnd;
    private String confirm;

    private LocalDateTime writeAt;



    public ProductResponseDto(Product product)
    {
        this.id=product.getId();
        this.memberName=product.getMember().getMemberName();
        this.productName=product.getProductName();
        this.price=product.getPrice();
        this.content=product.getContent();
        this.imgUrl=product.getImgUrl();
        this.cateId=product.getCateId();
        this.writeAt=product.getModifiedAt();
        this.rentStart=product.getRentStart();
        this.rentEnd=product.getRentEnd();
        this.confirm=product.getConfirm();
    }

}
