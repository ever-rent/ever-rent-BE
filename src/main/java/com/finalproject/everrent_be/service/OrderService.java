package com.finalproject.everrent_be.service;

import com.finalproject.everrent_be.dto.OrderRequestDto;
import com.finalproject.everrent_be.dto.OrderResponseDto;
import com.finalproject.everrent_be.dto.ResponseDto;
import com.finalproject.everrent_be.exception.ErrorCode;
import com.finalproject.everrent_be.model.Member;
import com.finalproject.everrent_be.model.Order;
import com.finalproject.everrent_be.model.Product;
import com.finalproject.everrent_be.repository.OrderRepository;
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
    private final OrderRepository orderRepository;
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

        Order order= Order.builder()
                .member(member)
                .product(product)
                .buyStart(orderRequestDto.getBuyStart())
                .buyEnd(orderRequestDto.getBuyEnd())
                .confirm("1")
                .build();

        orderRepository.save(order);

        OrderResponseDto orderResponseDto=OrderResponseDto.builder()
                .memberName(order.getMember().getMemberName())
                .productName(order.getProduct().getProductName())
                .buyStart(order.getBuyStart())
                .buyEnd(order.getBuyEnd())
                .confirm(order.getConfirm())
                .build();

        return ResponseDto.is_Success(orderResponseDto);

    }


    public ResponseDto<?> confirmOrder(String orderId)
    {
        Optional<Order> optionalOrder=orderRepository.findById(Long.valueOf(orderId));
        Order order=optionalOrder.get();

        OrderResponseDto orderResponseDto=OrderResponseDto.builder()
                .id(order.getId())
                .memberName(order.getMember().getMemberName())
                .productName(order.getProduct().getProductName())
                .buyStart(order.getBuyStart())
                .buyEnd(order.getBuyEnd())
                .build();

        return ResponseDto.is_Success(orderResponseDto);
    }





    public boolean checkDuplicate(Product product,OrderRequestDto orderRequestDto)
    {
        List<Order> orders=orderRepository.findAllByProductId(Long.valueOf(product.getId()));

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date rentStart = null;
        Date rentEnd = null;
        Date buyStart = null;  //지금 보내는거
        Date buyEnd = null;
        Date orderStart=null;  //order entity에 해당되는 product 시간들(예약된 시간들)
        Date orderEnd=null;
        try {
            rentStart = sdf.parse(product.getRentStart());
            rentEnd = sdf.parse(product.getRentEnd());
            buyStart = sdf.parse(orderRequestDto.getBuyStart());
            buyEnd = sdf.parse(orderRequestDto.getBuyEnd());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(!rentStart.before(buyStart)){
            return false;
        }
        if(!rentEnd.after(buyEnd)){
            return false;
        }
        if(!buyStart.before(buyEnd)){
            return false;
        }

        for(Order order:orders)
        {
            try{
                orderStart = sdf.parse(order.getBuyStart());
                orderEnd = sdf.parse(order.getBuyEnd());
                if(!buyEnd.before(orderStart))
                {
                    return false;
                }
                if(!buyStart.after(orderEnd))
                {
                    return false;
                }
            }catch (ParseException e) {
                e.printStackTrace();
            }

        }
        return true;
    }


}
