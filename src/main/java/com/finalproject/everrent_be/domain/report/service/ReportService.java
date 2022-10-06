package com.finalproject.everrent_be.domain.report.service;

import com.finalproject.everrent_be.domain.member.model.Member;
import com.finalproject.everrent_be.domain.member.repository.MemberRepository;
import com.finalproject.everrent_be.domain.member.service.MemberService;
import com.finalproject.everrent_be.domain.product.model.Product;
import com.finalproject.everrent_be.domain.product.repository.ProductRepository;
import com.finalproject.everrent_be.domain.report.dto.RpRequestDto;
import com.finalproject.everrent_be.domain.report.dto.RpResponseDto;
import com.finalproject.everrent_be.domain.report.model.RpUser;
import com.finalproject.everrent_be.domain.report.model.RpPost;
import com.finalproject.everrent_be.domain.report.repository.RpMemberRepository;
import com.finalproject.everrent_be.domain.report.repository.RpPostRepository;
import com.finalproject.everrent_be.global.common.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
@RequiredArgsConstructor
public class ReportService {

    private final MemberService memberService;
    private final ProductRepository productRepository;

    private final MemberRepository memberRepository;
    private final RpPostRepository rpPostRepository;
    private final RpMemberRepository rpMemberRepository;


    public ResponseDto<?> createProductReport(String productId, RpRequestDto requestDto) {
        Long whoisRpId=memberService.getMemberfromContext().getId();
        Product product=productRepository.findById(Long.valueOf(productId)).orElseThrow(
                () -> new IllegalArgumentException("해당 상품이 존재하지 않습니다.")
        );
        RpPost rpPost =new RpPost(product,requestDto.getRpreason(),whoisRpId);
        rpPostRepository.save(rpPost);
        RpResponseDto rpResponseDto=new RpResponseDto(rpPost);
        return ResponseDto.is_Success(rpResponseDto);

    }


    public ResponseDto<?> createMemberReport(String rtId, RpRequestDto requestDto) {
        Long whoisRpId=memberService.getMemberfromContext().getId();
        Member rpMember=memberRepository.findById(Long.valueOf(rtId)).orElseThrow(
                () -> new IllegalArgumentException("해당 유저가 존재하지 않습니다.")
        );
        RpUser rpUser =new RpUser(rpMember,requestDto.getRpreason(),whoisRpId);
        rpMemberRepository.save(rpUser);
        RpResponseDto rpResponseDto=new RpResponseDto(rpUser);
        return ResponseDto.is_Success(rpResponseDto);

    }
}
