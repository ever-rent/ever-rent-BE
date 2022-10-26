package com.finalproject.everrent_be.domain.product.controller;

import static org.junit.jupiter.api.Assertions.*;

import com.finalproject.everrent_be.domain.member.model.Member;
import com.finalproject.everrent_be.domain.product.dto.ProductRequestDto;
import com.finalproject.everrent_be.domain.product.model.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
class ProductControllerTest {


        @Test
        @DisplayName("product객체 생성 성공")
        void createProduct_Normal() {
// given

            ProductRequestDto requestDto= ProductRequestDto.builder()
                    .productName("제목")
                    .price("2000")
                    .content("내용")
                    .cateId("1")
                    .rentStart("2022-11-10")
                    .rentEnd("2022-11-20")
                    .location("서울특별시 중구")
                    .mapLocation("시청")
                    .build();

            Member member=Member.builder()
                    .memberName("닉네임쓰")
                    .password("12341234")
                    .email("test1@test.test")
                    .mainAddress("서울특별시 중구")
                    .subAddress("경기도 용인시")
                    .build();
            String imageUrl = "1234.jpg";
            LocalDate rentStart= StrToLocalDate("2022-11-10");
            LocalDate rentEnd= StrToLocalDate("2022-11-20");


// when
            Product product = new Product(requestDto, member,imageUrl,rentStart,rentEnd);

// then
            assertNull(product.getId());
            assertEquals("제목", product.getProductName());
            assertEquals("2000", product.getPrice());
            assertEquals("내용", product.getContent());
            assertEquals("1", product.getCateId());
            assertEquals("2022-11-10", LocalDateToStr(product.getRentStart()));
            assertEquals("2022-11-20", LocalDateToStr(product.getRentEnd()));
        }
            public String LocalDateToStr(LocalDate localDate){
            return localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));


        }
    public LocalDate StrToLocalDate(String string){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(string,formatter);
        return date;
    }



    }

