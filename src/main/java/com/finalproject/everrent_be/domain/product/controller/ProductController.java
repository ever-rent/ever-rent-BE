package com.finalproject.everrent_be.domain.product.controller;

import com.finalproject.everrent_be.domain.product.service.ProductService;
import com.finalproject.everrent_be.domain.product.dto.ProductRequestDto;
import com.finalproject.everrent_be.global.common.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    //메인페이지
    @GetMapping("/products")
    public ResponseDto<?> getAllProduct(@RequestParam String page){
        return productService.getAllProduct(page);
    }


    //상세페이지 조회
    @GetMapping("/products/{productId}")
    public ResponseDto<?> getProduct(@PathVariable String productId){
        return productService.getProduct(productId);
    }

    //카테고리 별 분류
    @GetMapping("/categories/{categoryId}")
    public ResponseDto<?> getFromCategory(@PathVariable String categoryId,@RequestParam String page)
    {
        return productService.getFromCategory(categoryId,page);
    }

    //상세페이지 작성
    @PostMapping("/products")
    public ResponseDto<?> createProduct(@RequestPart List<MultipartFile> multipartFiles, @RequestPart ProductRequestDto requestDto,
                                        HttpServletRequest request) {
        return productService.createProduct(multipartFiles, requestDto, request);
    }

    //상세페이지 수정
    @PutMapping("/products/{productId}")
    public ResponseDto<?> updateProduct(@PathVariable String productId,@RequestPart MultipartFile[] multipartFiles, @RequestPart ProductRequestDto requestDto, HttpServletRequest request){
        return productService.updateProduct(productId,multipartFiles,requestDto,request);
    }


    //상세페이지 삭제
    @DeleteMapping("/products/{productId}")
    public ResponseDto<?> deleteProduct(@PathVariable String productId,HttpServletRequest request){
        return productService.deleteProduct(productId,request);
    }



}
