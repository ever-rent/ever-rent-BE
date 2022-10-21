package com.finalproject.everrent_be.domain.rating.service;


import com.finalproject.everrent_be.domain.member.model.Member;
import com.finalproject.everrent_be.domain.member.repository.MemberRepository;
import com.finalproject.everrent_be.domain.member.service.MemberService;
import com.finalproject.everrent_be.domain.rating.dto.RatingRequestDto;
import com.finalproject.everrent_be.domain.rating.dto.RatingResponseDto;
import com.finalproject.everrent_be.global.common.ResponseDto;
import com.finalproject.everrent_be.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RatingService {

    private final MemberRepository memberRepository;
    private final MemberService memberService;
    @Transactional
    public ResponseDto<?> putRating(RatingRequestDto ratingRequestDto)
    {
        Member member=memberService.getMemberfromContext();
        Member othermember=memberRepository.findMemberById(Long.valueOf(ratingRequestDto.getMemberId()));

        if(member.getEmail().equals(othermember.getEmail()))
        {
            return ResponseDto.is_Fail(ErrorCode.INVALID_MEMBER);
        }

        othermember.updateRating(Long.valueOf(ratingRequestDto.getRating()));

        return ResponseDto.is_Success(RatingResponseDto.builder()
                .rating(othermember.getRating())
                .build());
    }

    public ResponseDto<?> getRating(String memberId)
    {
        Member member=memberRepository.findMemberById(Long.valueOf(memberId));

        return ResponseDto.is_Success(RatingResponseDto.builder()
                .rating(member.getRating())
                .build());
    }

}
