package com.finalproject.everrent_be.service;

import com.finalproject.everrent_be.dto.OrderRequestDto;
import com.finalproject.everrent_be.dto.OrderResponseDto;

import com.finalproject.everrent_be.dto.ResponseDto;
import com.finalproject.everrent_be.exception.ErrorCode;

import com.finalproject.everrent_be.model.Member;
import com.finalproject.everrent_be.model.OrderList;
import com.finalproject.everrent_be.model.Product;
import com.finalproject.everrent_be.model.Status;
import com.finalproject.everrent_be.repository.OrderListRepository;
import com.finalproject.everrent_be.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static com.finalproject.everrent_be.exception.ErrorCode.*;

@RequiredArgsConstructor
@Service
public class OrderService {

    private final MemberService memberService;
    private final ProductRepository productRepository;
    private final OrderListRepository orderListRepository;

    @Transactional
    public ResponseDto<?> creatOrder(String productId, OrderRequestDto orderRequestDto)
    {
        Member member=memberService.getMemberfromContext();
        Product product=productRepository.findById(Long.valueOf(productId)).orElseThrow(
                () -> new IllegalArgumentException("해당 상품이 존재하지 않습니다.")
        );

        if(member.getMemberName()==product.getMember().getMemberName())
        {
            return ResponseDto.is_Fail(ErrorCode.INVALID_CREATE);
        }

        ///예외처리, 나중에 함수로 빼기
        List<OrderList> orderLists = product.getOrderLists();

        int size=orderLists.size();

        LocalDate rentStart=product.getRentStart();
        LocalDate rentEnd=product.getRentEnd();
        LocalDate buyStart=StrToLocalDate(orderRequestDto.getBuyStart());
        LocalDate buyEnd=StrToLocalDate(orderRequestDto.getBuyEnd());

        //판매자가 작성한 렌트가능시간과 order시간과 맞지 않을 때
        if(!rentStart.isBefore(buyStart)||!rentEnd.isAfter(buyEnd)){
            return ResponseDto.is_Fail(INVALID_ORDER_IN);
        }
        //order start,end 시간순서가 맞지 않을 때
        if(!buyStart.isBefore(buyEnd)){
            return ResponseDto.is_Fail(INVALID_TIME_SEQUENCE);
        }

        //해당 product에 예약되어있는 시간들 중복 확인
        if(size>0){
            for(OrderList orderList:orderLists){
                LocalDate orderStart=orderList.getBuyStart();
                LocalDate orderEnd=orderList.getBuyEnd();
                if(!(buyStart.isAfter(orderEnd)||buyEnd.isBefore(orderStart)))
                {
                    return ResponseDto.is_Fail(INVALID_ORDER);
                }
            }
        }

        OrderList orderList = new OrderList();
        orderList = OrderList.builder()
                .member(member)
                .product(product)
                .buyStart(orderList.StrToLocalDate(orderRequestDto.getBuyStart()))
                .buyEnd(orderList.StrToLocalDate(orderRequestDto.getBuyEnd()))
                .status(Status.WAITING)
                .build();

        orderListRepository.save(orderList);

        OrderResponseDto orderResponseDto=new OrderResponseDto(orderList);

        return ResponseDto.is_Success(orderResponseDto);

    }


    public ResponseDto<?> confirmOrder(String orderId)
    {
        Optional<OrderList> optionalOrder= orderListRepository.findById(Long.valueOf(orderId));
        OrderList orderList =optionalOrder.get();

        OrderResponseDto orderResponseDto=new OrderResponseDto(orderList);

        return ResponseDto.is_Success(orderResponseDto);
    }


    public LocalDate StrToLocalDate(String string){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(string,formatter);
        return date;
    }
}
