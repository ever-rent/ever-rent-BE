package com.finalproject.everrent_be.domain.order.controller;

import com.finalproject.everrent_be.domain.member.model.Member;
import com.finalproject.everrent_be.domain.member.service.MemberService;
import com.finalproject.everrent_be.domain.order.dto.OrderRequestDto;
import com.finalproject.everrent_be.domain.order.dto.OrderResponseDto;
import com.finalproject.everrent_be.domain.order.model.OrderList;
import com.finalproject.everrent_be.domain.order.repository.OrderListRepository;
import com.finalproject.everrent_be.domain.product.model.Product;
import com.finalproject.everrent_be.global.common.ResponseDto;
import com.finalproject.everrent_be.domain.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    public final MemberService memberService;
    public final OrderListRepository orderListRepository;

    //예약 작성
    @PostMapping("/orders/{productId}")
    public ResponseDto<?> createOrder(@PathVariable String productId, @RequestBody OrderRequestDto orderRequestDto)
    {
        Member member= memberService.getMemberfromContext();
        List<OrderList> myords=orderListRepository.findAllByMember(member);
        //뱃지2-첫예약
        if(myords.size()==0){
            member.setBadges(2,"1");
        }
        return orderService.creatOrder(productId,orderRequestDto);
    }

    @GetMapping("/orders/{orderId}")
    public ResponseDto<?> confirmOrder(@PathVariable String orderId)
    {
        return orderService.confirmOrder(orderId);
    }

}

