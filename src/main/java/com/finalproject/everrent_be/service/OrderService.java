package com.finalproject.everrent_be.service;

import com.finalproject.everrent_be.dto.OrderRequestDto;
import com.finalproject.everrent_be.dto.OrderResponseDto;
import com.finalproject.everrent_be.dto.ProductResponseDto;
import com.finalproject.everrent_be.dto.ResponseDto;
import com.finalproject.everrent_be.exception.ErrorCode;
import com.finalproject.everrent_be.jwt.TokenProvider;
import com.finalproject.everrent_be.model.Member;
import com.finalproject.everrent_be.model.Order;
import com.finalproject.everrent_be.model.Product;
import com.finalproject.everrent_be.repository.OrderRepository;
import com.finalproject.everrent_be.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    public final ProductRepository productRepository;
    public final MemberService memberService;
    public final FileUploadService fileUploadService;
    public final OrderRepository orderRepository;
    public final TokenProvider tokenProvider;


    public ResponseDto<?> creatOrder(String productId, OrderRequestDto orderRequestDto, HttpServletRequest request)
    {
        Member member=memberService.getMemberfromContext();
        Product product=productRepository.findById(Long.valueOf(productId)).orElseThrow(
                () -> new IllegalArgumentException("해당 상품이 존재하지 않습니다.")
        );
        List<Order> orders=orderRepository.findAllByProductId(Long.valueOf(productId));

        if(member.getMemberName()==product.getMember().getMemberName())
        {
            return ResponseDto.is_Fail(ErrorCode.INVALID_CREATE);
        }

        /////////////
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date rentStart = null;
        Date rentEnd = null;
        Date buyStart = null;
        Date buyEnd = null;
        try {
            rentStart = sdf.parse(product.getRentStart());
            rentEnd = sdf.parse(product.getRentEnd());
            buyStart = sdf.parse(orderRequestDto.getBuyStart());
            buyEnd = sdf.parse(orderRequestDto.getBuyEnd());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //판매자가 올린 rent기한과 비교

        if(!rentStart.before(buyStart)){
            return ResponseDto.is_Fail(ErrorCode.INVALID_START_DATE);
        }
        if(!rentEnd.after(buyEnd)){
            return ResponseDto.is_Fail(ErrorCode.INVALID_END_DATE);
        }


        //선택한 예약일 사이 누군가 빌린 기간이 있을 경우
        List<Order> orderList=orderRepository.findAllByProductId(Long.valueOf(productId));

        for(Order order:orderList){
            Date otherStart=null;
            Date otherEnd=null;
            try{
                otherStart = sdf.parse(order.getBuyStart());
                otherEnd = sdf.parse(order.getBuyEnd());

            }catch (ParseException e) {
                e.printStackTrace();
            }

            if(buyStart.before(otherEnd)){
                return ResponseDto.is_Fail(ErrorCode.INVALID_START_DATE);
            }

            if(buyEnd.after(otherStart)){
                return ResponseDto.is_Fail(ErrorCode.INVALID_END_DATE);
            }}


        Order order= Order.builder()
                .member(member)
                .product(product)
                .buyStart(orderRequestDto.getBuyStart())
                .buyEnd(orderRequestDto.getBuyEnd())
                .confirm("1")
                .build();

        orderRepository.save(order);
        OrderResponseDto orderResponseDto=new OrderResponseDto(order);
        return ResponseDto.is_Success(orderResponseDto); //?product랑 같이 보내줘야 하는지?
    }



}
