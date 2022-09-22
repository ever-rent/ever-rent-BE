package com.finalproject.everrent_be.service;

import com.finalproject.everrent_be.dto.OrderRequestDto;
import com.finalproject.everrent_be.dto.OrderResponseDto;

import com.finalproject.everrent_be.dto.ResponseDto;
import com.finalproject.everrent_be.exception.ErrorCode;

import com.finalproject.everrent_be.model.Member;
import com.finalproject.everrent_be.model.OrderList;
import com.finalproject.everrent_be.model.Product;
import com.finalproject.everrent_be.repository.OrderListRepository;
import com.finalproject.everrent_be.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.finalproject.everrent_be.exception.ErrorCode.INVALID_TIMESETTING;

@RequiredArgsConstructor
@Service
public class OrderService {

    private final MemberService memberService;
    private final ProductRepository productRepository;
    private final OrderListRepository orderListRepository;
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


        if(!checkDuplicate(product,orderRequestDto))
        {
            return ResponseDto.is_Fail(INVALID_TIMESETTING);
        }

        OrderList orderList = OrderList.builder()
                .member(member)
                .product(product)
                .buyStart(orderRequestDto.getBuyStart())
                .buyEnd(orderRequestDto.getBuyEnd())
                .confirm("1")
                .build();

        orderListRepository.save(orderList);

        OrderResponseDto orderResponseDto=OrderResponseDto.builder()
                .id(orderList.getId())
                .memberName(orderList.getMember().getMemberName())
                .productName(orderList.getProduct().getProductName())
                .buyStart(orderList.getBuyStart())
                .buyEnd(orderList.getBuyEnd())
                .confirm(orderList.getConfirm())
                .build();

        return ResponseDto.is_Success(orderResponseDto);

    }


    public ResponseDto<?> confirmOrder(String orderId)
    {
        Optional<OrderList> optionalOrder= orderListRepository.findById(Long.valueOf(orderId));
        OrderList orderList =optionalOrder.get();

        OrderResponseDto orderResponseDto=OrderResponseDto.builder()
                .id(orderList.getId())
                .memberName(orderList.getMember().getMemberName())
                .productName(orderList.getProduct().getProductName())
                .buyStart(orderList.getBuyStart())
                .buyEnd(orderList.getBuyEnd())
                .build();

        return ResponseDto.is_Success(orderResponseDto);
    }





    public boolean checkDuplicate(Product product,OrderRequestDto orderRequestDto)
    {
        List<OrderList> orderLists = orderListRepository.findAllByProduct(product);

        System.out.println(111);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date rentStart = null;
        Date rentEnd = null;
        Date buyStart = null;  //지금 보내는거
        Date buyEnd = null;

        System.out.println(222);
        try {
            rentStart = sdf.parse(product.getRentStart());
            rentEnd = sdf.parse(product.getRentEnd());
            buyStart = sdf.parse(orderRequestDto.getBuyStart());
            buyEnd = sdf.parse(orderRequestDto.getBuyEnd());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println(333);

        if(!rentStart.before(buyStart)){
            System.out.println(000000000000);
            return false;
        }
        if(!rentEnd.after(buyEnd)){
            System.out.println(11111);
            return false;
        }
        if(!buyStart.before(buyEnd)){
            System.out.println(22222222);
            return false;
        }

        for(OrderList orderList : orderLists)
        {
            Date orderStart=null;  //order entity에 해당되는 product 시간들(예약된 시간들)
            Date orderEnd=null;

            try{
                orderStart = sdf.parse(orderList.getBuyStart());
                orderEnd = sdf.parse(orderList.getBuyEnd());
                if(orderStart.after(buyEnd)) //orderStart 2022-09-21, "buyEnd":"2022-09-30"
                {
                    System.out.println(orderStart);
                    System.out.println(buyEnd);
                    System.out.println(33333);
                    return false;
                }
                if(orderEnd.before(buyStart))
                {
                    System.out.println(orderEnd);
                    System.out.println(buyStart);
                    System.out.println(444444);
                    return false;
                }
            }catch (ParseException e) {
                e.printStackTrace();
            }

        }
        return true;
    }
}
