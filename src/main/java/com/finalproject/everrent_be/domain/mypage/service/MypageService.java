package com.finalproject.everrent_be.domain.mypage.service;


import com.finalproject.everrent_be.domain.order.dto.OrderResponseDto;
import com.finalproject.everrent_be.domain.order.model.OrderList;
import com.finalproject.everrent_be.domain.product.model.Product;
import com.finalproject.everrent_be.domain.wishlist.dto.WishListResponseDto;
import com.finalproject.everrent_be.domain.wishlist.model.WishList;
import com.finalproject.everrent_be.gloabl.common.ResponseDto;
import com.finalproject.everrent_be.domain.member.model.Member;
import com.finalproject.everrent_be.domain.member.service.MemberService;
import com.finalproject.everrent_be.gloabl.common.Status;
import com.finalproject.everrent_be.domain.order.repository.OrderListRepository;
import com.finalproject.everrent_be.domain.mypage.dto.MypageResponseDto;
import com.finalproject.everrent_be.domain.product.dto.ProductResponseDto;
import com.finalproject.everrent_be.domain.wishlist.repository.WishListRepository;
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
    public final WishListRepository wishListRepository;


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
            if(product.getStatus().equals(Status.EXPIRATION))
            {
                productResponseDtos.add(new ProductResponseDto(product));
            }
        }
        return ResponseDto.is_Success(productResponseDtos);
    }

    public ResponseDto<?> getMypgWait()
    {
        Member member=memberService.getMemberfromContext();
        List<Product> products=member.getProducts();
        List<OrderResponseDto> orderResponseDtos=new ArrayList<>();
        for(Product product:products)
        {
            List<OrderList> orderLists=product.getOrderLists();
            for(OrderList orderList:orderLists)
            {
                if(orderList.getStatus().equals(Status.WAITING))
                {
                    orderResponseDtos.add(new OrderResponseDto(orderList));
                }
            }

        }

        return ResponseDto.is_Success(orderResponseDtos);
    }

    public ResponseDto<?> getMypgConfirm()
    {
        Member member=memberService.getMemberfromContext();
        List<Product> products=member.getProducts();
        List<OrderResponseDto> orderResponseDtos=new ArrayList<>();
        for(Product product:products)
        {
            List<OrderList> orderLists=product.getOrderLists();
            for(OrderList orderList:orderLists)
            {
                if(orderList.getStatus().equals(Status.CONFIRMATION))
                {
                    orderResponseDtos.add(new OrderResponseDto(orderList));
                }
            }

        }

        return ResponseDto.is_Success(orderResponseDtos);
    }


    public ResponseDto<?> getMypgMyRent()
    {
        Member member=memberService.getMemberfromContext();
        List<OrderList> orderLists=member.getOrderLists();
        List<OrderResponseDto> orderResponseDtos=new ArrayList<>();
        for(OrderList orderList:orderLists)
        {
            if(orderList.getStatus().equals(Status.CONFIRMATION))
            {
                orderResponseDtos.add(new OrderResponseDto(orderList));
            }


        }

        return ResponseDto.is_Success(orderResponseDtos);
    }

    public ResponseDto<?> getMyWishs()
    {
        Member member=memberService.getMemberfromContext();
        List<WishList> wishLists=member.getWishLists();
        List<WishListResponseDto> wishListResponseDtos=new ArrayList<>();
        for(WishList wishList:wishLists)
        {
            wishListResponseDtos.add(new WishListResponseDto(wishList));
        }

        return ResponseDto.is_Success(wishListResponseDtos);
    }


    @Transactional
    public ResponseDto<?> allowOrder(String orderId)
    {
        Optional<OrderList> optionalOrderList=orderListRepository.findById(Long.valueOf(orderId));
        OrderList orderList=optionalOrderList.get();
        orderList.updateStatus(Status.CONFIRMATION);
        OrderResponseDto orderResponseDto=new OrderResponseDto(orderList);

        return ResponseDto.is_Success(orderResponseDto);
    }

    public ResponseDto<?> getMyInfo()
    {
        Member member=memberService.getMemberfromContext();
        MypageResponseDto mypageResponseDto=new MypageResponseDto(member);
        return ResponseDto.is_Success(mypageResponseDto);
    }


}
