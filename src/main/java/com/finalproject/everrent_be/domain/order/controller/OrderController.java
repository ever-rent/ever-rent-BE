package com.finalproject.everrent_be.domain.order.controller;

import com.finalproject.everrent_be.domain.order.dto.OrderRequestDto;
import com.finalproject.everrent_be.gloabl.common.ResponseDto;
import com.finalproject.everrent_be.domain.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    //예약 작성
    @PostMapping("/orders/{productId}")
    public ResponseDto<?> createOrder(@PathVariable String productId, @RequestBody OrderRequestDto orderRequestDto)
    {
        return orderService.creatOrder(productId,orderRequestDto);
    }

    @GetMapping("/orders/{orderId}")
    public ResponseDto<?> confirmOrder(@PathVariable String orderId)
    {
        return orderService.confirmOrder(orderId);
    }

}

