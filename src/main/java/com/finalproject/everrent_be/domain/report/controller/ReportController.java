package com.finalproject.everrent_be.domain.report.controller;

import com.finalproject.everrent_be.gloabl.common.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReportController {

    //멤버 신고페이지
    @PostMapping("/products/report/{memberId}")
    public ResponseDto<?> createMemberReport(){
        return null;
    }

}
