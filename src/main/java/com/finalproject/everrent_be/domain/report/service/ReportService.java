package com.finalproject.everrent_be.domain.report.service;

import com.finalproject.everrent_be.domain.member.model.Member;
import com.finalproject.everrent_be.domain.member.service.MemberService;
import com.finalproject.everrent_be.domain.product.model.Product;
import com.finalproject.everrent_be.domain.product.repository.ProductRepository;
import com.finalproject.everrent_be.domain.report.dto.ReportRequestDto;
import com.finalproject.everrent_be.domain.report.model.Report;
import com.finalproject.everrent_be.domain.report.repository.ReportRepository;
import com.finalproject.everrent_be.global.common.ResponseDto;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class ReportService {
    /*
    private final MemberService memberService;
    private final ProductRepository productRepository;

    public ResponseDto<?> createProductReport(String productId, ReportRequestDto requestDto) {
        Member member=memberService.getMemberfromContext();
        Product product=productRepository.findById(Long.valueOf(productId)).orElseThrow(
                () -> new IllegalArgumentException("해당 상품이 존재하지 않습니다.")
        );

        Report report=new Report(member,requestDto.getRtreason());
        ReportRepository.save(report);
    }*/

}
