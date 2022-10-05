package com.finalproject.everrent_be.domain.mypage.controller;


import com.finalproject.everrent_be.domain.mypage.service.MypageService;
import com.finalproject.everrent_be.gloabl.common.ResponseDto;
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

    @GetMapping("/mypages/myinfos")
    public ResponseDto<?> getMyInfo()
    {
        return mypageService.getMyInfo();

    }

    @GetMapping("/mypages/myWishs")
    public ResponseDto<?> getMyWishs()
    {
        return mypageService.getMyWishs();

    }

    @PutMapping("/mypages/confirms/{orderId}")
    public ResponseDto<?> allowOrder(@PathVariable String orderId)
    {
        return mypageService.allowOrder(orderId);
    }
}
