package com.finalproject.everrent_be.domain.product.service;

import com.finalproject.everrent_be.domain.product.dto.ProductRequestDto;
import com.finalproject.everrent_be.domain.product.model.Product;
import com.finalproject.everrent_be.domain.product.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mock.web.MockMultipartFile;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Array;

import static org.junit.jupiter.api.Assertions.*;

class ProductServiceTest {
/*
    @Test
    @RunWith(MockitoJUnitRunner.class)
    @DisplayName("상품 변경")
    void updateProduct_Normal() throws IOException {
        //given
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




        MockMultipartFile multipartFile=new MockMultipartFile("image",
                "test.png",
                "image/png",
                new FileInputStream("dog.jpg"));
        MockMultipartFile[] mockMultipartFiles=new MockMultipartFile[1];
        mockMultipartFiles[0]=multipartFile;



        ProductService productService = new ProductService();
        Product product=productService.updateProduct(777L,mockMultipartFiles,requestDto,); //http서블렛

    }*/


}

