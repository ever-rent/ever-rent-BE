package com.finalproject.everrent_be.domain.wishlist.controller;

import com.finalproject.everrent_be.domain.wishlist.service.WishService;
import com.finalproject.everrent_be.global.common.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class WishController {
    private final WishService wishService;
    @PostMapping("/products/wishlists/{productId}")
    public ResponseDto<?> putWishList(@PathVariable String productId)
    {
        return wishService.putWishList(productId);
    }



}
