package com.finalproject.everrent_be.domain.search.service;


import com.finalproject.everrent_be.domain.product.dto.ProductResponseDto;
import com.finalproject.everrent_be.domain.product.model.Product;
import com.finalproject.everrent_be.domain.product.repository.ProductRepository;
import com.finalproject.everrent_be.domain.product.service.ProductService;
import com.finalproject.everrent_be.domain.search.dto.SearchResponseDto;
import com.finalproject.everrent_be.domain.wishlist.repository.WishListRepository;
import com.finalproject.everrent_be.global.common.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final ProductRepository productRepository;
    private final WishListRepository wishListRepository;
    public ResponseDto<?> searchProducts(String keyword)
    {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Product> products = productRepository.findAll();
        List<SearchResponseDto> searchResponseDtos=new ArrayList<>();
        for(Product product:products)
        {
            boolean heart=false;
            if(product.getProductName().contains(keyword)||product.getContent().contains(keyword)||
                    product.getMember().getMemberName().contains(keyword)||product.getLocation().contains(keyword))
            {
                if (userId.equals("anonymousUser")){
                    heart=false;
                }
                else if (wishListRepository.findByMemberIdAndProductId(Long.valueOf(userId), product.getId()) != null) {
                    heart = true;
                }

                searchResponseDtos.add(new SearchResponseDto(product,heart));
            }
        }

        return ResponseDto.is_Success(searchResponseDtos);
    }

}
