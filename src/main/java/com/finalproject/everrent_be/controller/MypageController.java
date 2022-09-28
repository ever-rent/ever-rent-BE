package com.finalproject.everrent_be.controller;


import com.finalproject.everrent_be.dto.ResponseDto;
import com.finalproject.everrent_be.service.MypageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MypageController {

    private final MypageService mypageService;

    @GetMapping("/mypages/lists")
    public ResponseDto<?> getMypgLists()
    {
        return mypageService.getMypgLists();
    }
    @GetMapping("/mypages/expired")
    public ResponseDto<?> getMypgExpired()
    {
        return mypageService.getMypgExpired();
    }

    @GetMapping("/mypages/waitlists")
    public ResponseDto<?> getMypgWait()
    {
        return mypageService.getMypgWait();
    }

    @GetMapping("/mypages/confirms")
    public ResponseDto<?> getMypgConfirm()
    {
        return mypageService.getMypgConfirm();
    }

    @GetMapping("/mypages/myrent")
    public ResponseDto<?> getMypgMyRent()
    {
        return mypageService.getMypgMyRent();
    }


    @PutMapping("/mypages/confirms/{orderId}")
    public ResponseDto<?> allowOrder(@PathVariable String orderId)
    {
        return mypageService.allowOrder(orderId);
    }
}
