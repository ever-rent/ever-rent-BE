package com.finalproject.everrent_be.domain.search.controller;


import com.finalproject.everrent_be.domain.search.service.SearchService;
import com.finalproject.everrent_be.global.common.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @GetMapping("/searchs")
    public ResponseDto<?> searchProducts(@RequestParam String keyword)
    {
        return searchService.searchProducts(keyword);
    }


}
