package com.finalproject.everrent_be.dto;


import com.finalproject.everrent_be.model.Order;
import com.finalproject.everrent_be.model.Product;
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

    public OrderResponseDto(Order order)
    {
        this.buyStart=order.getBuyStart();
        this.buyEnd=order.getBuyEnd();
        this.confirm=order.getConfirm();
    }

}
