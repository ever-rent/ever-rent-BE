package com.finalproject.everrent_be.controller;

import com.finalproject.everrent_be.dto.OrderRequestDto;
import com.finalproject.everrent_be.dto.ResponseDto;
import com.finalproject.everrent_be.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    //예약 작성
    @PostMapping("/auth/orders/{productId}")
    public ResponseDto<?> createOrder(@PathVariable String productId, @RequestBody OrderRequestDto orderRequestDto)
    {
        return orderService.creatOrder(productId,orderRequestDto);
    }

    @GetMapping("/auth/orders/{orderId}")
    public ResponseDto<?> confirmOrder(@PathVariable String orderId)
    {
        return orderService.confirmOrder(orderId);
    }

}

