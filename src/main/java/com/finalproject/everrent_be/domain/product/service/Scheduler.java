package com.finalproject.everrent_be.domain.product.service;


import com.finalproject.everrent_be.domain.order.service.OrderService;
import com.finalproject.everrent_be.domain.product.repository.ProductRepository;
import com.finalproject.everrent_be.domain.product.model.Product;
import com.finalproject.everrent_be.global.common.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor // final 멤버 변수를 자동으로 생성합니다.
@Component // 스프링이 필요 시 자동으로 생성하는 클래스 목록에 추가합니다.
public class Scheduler {

    private final ProductRepository productRepository;
    private final OrderService orderService;

    // 초, 분, 시, 일, 월, 주 순서
    @Scheduled(cron = "0 27 20 * * *")
    public void deleteEmptyComment() throws InterruptedException {

        List<Product> products = productRepository.findAll();
        LocalDate now = LocalDate.now();


        boolean check=false;
        for(Product product:products)
        {
            if(product.getRentEnd().isBefore(now))
            {
                product.updateStatus(Status.EXPIRATION);
                productRepository.save(product);
                check=true;
            }
        }
        if(check==false){
            System.out.println("만료된 게시글이 없습니다.");
        }

    }
}