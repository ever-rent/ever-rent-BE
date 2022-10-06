package com.finalproject.everrent_be.domain.report.controller;

import com.finalproject.everrent_be.domain.report.dto.ReportRequestDto;
import com.finalproject.everrent_be.domain.report.service.ReportService;
import com.finalproject.everrent_be.global.common.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReportController {

    /*
    private final ReportService reportService;

    //멤버 신고페이지
    @PostMapping("/report/{userId}")
    public ResponseDto<?> createMemberReport(@PathVariable String memberId,@RequestBody ReportRequestDto requestDto){
        return null;
    }

    //게시글 신고페이지
    @PostMapping("/report/{productId}")
    public ResponseDto<?> createProductReport(@PathVariable String productId,@RequestBody ReportRequestDto requestDto){
        return reportService.createProductReport(productId,requestDto);
    }
     */

}
