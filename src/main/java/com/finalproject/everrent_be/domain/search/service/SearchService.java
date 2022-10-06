package com.finalproject.everrent_be.domain.search.service;


import com.finalproject.everrent_be.domain.product.dto.ProductResponseDto;
import com.finalproject.everrent_be.domain.product.model.Product;
import com.finalproject.everrent_be.domain.product.repository.ProductRepository;
import com.finalproject.everrent_be.domain.product.service.ProductService;
import com.finalproject.everrent_be.global.common.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final ProductRepository productRepository;

    public ResponseDto<?> searchProducts(String keyword)
    {
        List<Product> products = productRepository.findAll();
        List<ProductResponseDto> productResponseDtos=new ArrayList<>();
        for(Product product:products)
        {
            if(product.getProductName().contains(keyword)||product.getContent().contains(keyword)||
                    product.getMember().getMemberName().contains(keyword)||product.getLocation().contains(keyword))
            {
                productResponseDtos.add(new ProductResponseDto(product));
            }
        }

        return ResponseDto.is_Success(productResponseDtos);
    }

}
