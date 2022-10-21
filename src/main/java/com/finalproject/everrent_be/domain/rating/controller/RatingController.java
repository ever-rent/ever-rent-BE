package com.finalproject.everrent_be.domain.rating.controller;


import com.finalproject.everrent_be.domain.rating.dto.RatingRequestDto;
import com.finalproject.everrent_be.domain.rating.service.RatingService;
import com.finalproject.everrent_be.global.common.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class RatingController {

    private final RatingService ratingService;


    @PutMapping("/ratings")
    public ResponseDto<?> putRating(@RequestBody RatingRequestDto ratingRequestDto)
    {

        return ratingService.putRating(ratingRequestDto);

    }

    @GetMapping("/ratings/{memberId}")
    public ResponseDto<?> getRating(@PathVariable String memberId)
    {
        return ratingService.getRating(memberId);
    }


}
