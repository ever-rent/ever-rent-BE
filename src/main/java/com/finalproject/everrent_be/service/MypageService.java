package com.finalproject.everrent_be.service;


import com.finalproject.everrent_be.dto.OrderResponseDto;
import com.finalproject.everrent_be.dto.ProductResponseDto;
import com.finalproject.everrent_be.dto.ResponseDto;
import com.finalproject.everrent_be.model.Member;
import com.finalproject.everrent_be.model.OrderList;
import com.finalproject.everrent_be.model.Product;
import com.finalproject.everrent_be.repository.OrderListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MypageService {

    private final MemberService memberService;
    private final OrderListRepository orderListRepository;


    public ResponseDto<?> getMypgLists()
    {
        Member member=memberService.getMemberfromContext();

        List<Product> products=member.getProducts();
        List<ProductResponseDto> productResponseDtos=new ArrayList<>();
        for(Product product:products)
        {
            productResponseDtos.add(new ProductResponseDto(product));
        }
        return ResponseDto.is_Success(productResponseDtos);
    }

    public ResponseDto<?> getMypgExpired()
    {
        Member member=memberService.getMemberfromContext();

        List<Product> products=member.getProducts();
        List<ProductResponseDto> productResponseDtos=new ArrayList<>();
        for(Product product:products)
        {
            if(product.getConfirm()=="2")
            {
                productResponseDtos.add(new ProductResponseDto(product));
            }
        }
        return ResponseDto.is_Success(productResponseDtos);
    }

    public ResponseDto<?> getMypgWait()
    {
        Member member=memberService.getMemberfromContext();
        List<OrderList> orderLists=member.getOrderLists();
        List<OrderResponseDto> orderResponseDtos=new ArrayList<>();
        for(OrderList orderList:orderLists)
        {
            if(orderList.getConfirm()=="1")
            {
                orderResponseDtos.add(new OrderResponseDto(orderList));
            }
        }
        return ResponseDto.is_Success(orderResponseDtos);
    }

    public ResponseDto<?> getMypgConfirm()
    {
        Member member=memberService.getMemberfromContext();
        List<OrderList> orderLists=member.getOrderLists();
        List<OrderResponseDto> orderResponseDtos=new ArrayList<>();
        for(OrderList orderList:orderLists)
        {
            if(orderList.getConfirm()=="2")
            {
                orderResponseDtos.add(new OrderResponseDto(orderList));
            }
        }
        return ResponseDto.is_Success(orderResponseDtos);
    }



    @Transactional
    public ResponseDto<?> allowOrder(String orderId)
    {
        Optional<OrderList> optionalOrderList=orderListRepository.findById(Long.valueOf(orderId));
        OrderList orderList=optionalOrderList.get();
        orderList.updateConfirm("2");
        OrderResponseDto orderResponseDto=new OrderResponseDto(orderList);

        return ResponseDto.is_Success(orderResponseDto);
    }






}
