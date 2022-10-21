package com.finalproject.everrent_be.domain.rating.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RatingRequestDto {

    private String memberId;
    private String rating;

}
