package com.finalproject.everrent_be.dto;


import com.finalproject.everrent_be.model.OrderList;
import com.finalproject.everrent_be.model.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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
    private Status status;

    public OrderResponseDto(OrderList orderList)
    {
        this.id=orderList.getId();
        this.memberName=orderList.getMember().getMemberName();
        this.productName=orderList.getProduct().getProductName();
        this.buyStart= LocalDateToStr(orderList.getBuyStart());
        this.buyEnd= LocalDateToStr(orderList.getBuyEnd());
        this.status= orderList.getStatus();
    }
    public String LocalDateToStr(LocalDate localDate){
        return localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

}
