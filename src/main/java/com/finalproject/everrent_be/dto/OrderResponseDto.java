package com.finalproject.everrent_be.dto;


import com.finalproject.everrent_be.model.OrderList;
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

    public OrderResponseDto(OrderList orderList)
    {
        this.memberName=orderList.getMember().getMemberName();
        this.productName=orderList.getProduct().getProductName();
        this.buyStart= orderList.getBuyStart();
        this.buyEnd= orderList.getBuyEnd();
        this.confirm= orderList.getConfirm();
    }

}
