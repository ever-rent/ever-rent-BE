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


        if(!checkDuplicate(product,orderRequestDto))
        {
            return ResponseDto.is_Fail(INVALID_TIMESETTING);
        }

        OrderList orderList = OrderList.builder()
                .member(member)
                .product(product)
                .buyStart(orderRequestDto.getBuyStart())
                .buyEnd(orderRequestDto.getBuyEnd())
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





    public boolean checkDuplicate(Product product,OrderRequestDto orderRequestDto)
    {
        List<OrderList> orderLists = product.getOrderLists();

        int size=orderLists.size();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date rentStart = null;
        Date rentEnd = null;
        Date buyStart = null;  //지금 보내는거
        Date buyEnd = null;

//        try {
//            rentStart = sdf.parse(product.getRentStart());
//            rentEnd = sdf.parse(product.getRentEnd());
//            buyStart = sdf.parse(orderRequestDto.getBuyStart());
//            buyEnd = sdf.parse(orderRequestDto.getBuyEnd());
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }

        if(!rentStart.before(buyStart)||!rentEnd.after(buyEnd)){
            return false;
        }
        if(!buyStart.before(buyEnd)){
            return false;
        }

        //order entity에 해당되는 product 시간들(예약된 시간들)
        Date orderStart=null;
        Date orderEnd=null;
        Date ordernextStart=null;

        if(size>0)
        {
            for(int i=0;i<size;i++)
            {
                try{
                    orderStart=sdf.parse(orderLists.get(i).getBuyStart());
                    orderEnd = sdf.parse(orderLists.get(i).getBuyEnd());
                    if(!(buyStart.after(orderEnd)||buyEnd.before(orderStart)))
                    {
                        return false;
                    }

                }catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }

        return true;
    }
}
