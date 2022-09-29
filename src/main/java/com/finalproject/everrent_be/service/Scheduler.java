package com.finalproject.everrent_be.service;


import com.finalproject.everrent_be.model.Product;
import com.finalproject.everrent_be.model.Status;
import com.finalproject.everrent_be.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor // final 멤버 변수를 자동으로 생성합니다.
@Component // 스프링이 필요 시 자동으로 생성하는 클래스 목록에 추가합니다.
public class Scheduler {

    private final ProductRepository productRepository;


    // 초, 분, 시, 일, 월, 주 순서
//    @Scheduled(cron = "0 01 00 * * *")
//    public void deleteEmptyComment() throws InterruptedException {
//
//        List<Product> products = productRepository.findAll();
//        LocalDate now = LocalDate.now();
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        Date nowDate= null;
//        Date expired = null;
//
//        boolean check=false;
//        for(Product product:products)
//        {
//            try{
//                nowDate = sdf.parse(String.valueOf(now));
//                expired = sdf.parse(product.getRentEnd());
//
//
//                if(expired.before(nowDate))
//                {
//                    product.updateStatus(Status.EXPIRATION);
//                    productRepository.save(product);
//                    check=true;
//                }
//            }catch (ParseException e) {
//                e.printStackTrace();
//            }
//        }
//        if(check==false){
//            System.out.println("만료된 게시글이 없습니다.");
//        }
//
//    }
}