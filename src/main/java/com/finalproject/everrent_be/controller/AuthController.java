package com.finalproject.everrent_be.controller;



import com.fasterxml.jackson.core.JsonProcessingException;

import com.finalproject.everrent_be.dto.*;
import com.finalproject.everrent_be.emailverified.service.RegisterMail;
import com.finalproject.everrent_be.oauth.model.OauthResponseModel;
import com.finalproject.everrent_be.oauth.service.GoogleService;
import com.finalproject.everrent_be.repository.MemberRepository;
import com.finalproject.everrent_be.service.AuthService;
import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;


//회원가입 로그인 로그아웃
@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final MemberRepository memberRepository;
    private final GoogleService googleService;
    private final RegisterMail registerMail;

    // 회원가입
    @PostMapping("/members/signups")
    public ResponseDto<?> signup(@RequestBody MemberRequestDto memberRequestDto) {
        return authService.signup(memberRequestDto);
    }

    //아이디 중복검사
    @PostMapping("/members/idchecks")
    public boolean idcheck(@RequestBody MemberCheckRequestDto checkRequestDto)
    {
        return authService.nicknameCheck(checkRequestDto);
    }

    // 이메일 중복검사
    @PostMapping("/members/emailchecks")
    public boolean emailCheck(@RequestBody EmailCheckRequestDto checkRequestDto)
    {
        return authService.emailCheck(checkRequestDto);
    }

    // 로그인
    @PostMapping("/members/logins")
    public ResponseDto<?> login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {
        TokenDto tokenDto = authService.login(loginRequestDto);
        response.setHeader("Authorization", "Bearer " + tokenDto.getAccessToken());
        response.setHeader("Refresh-Token", tokenDto.getRefreshToken());
        response.setHeader("Access-Token-Expire-Time", String.valueOf(tokenDto.getAccessTokenExpiresIn()));
        ResponseDto responseDto = ResponseDto.is_Success(memberRepository.findMemberByEmail(loginRequestDto.getEmail()));
        return responseDto;
    }

    //토큰 재발급
    @PostMapping("/members/reissues")
    public ResponseEntity<TokenDto> reissue(@RequestBody TokenRequestDto tokenRequestDto) {
        return ResponseEntity.ok(authService.reissue(tokenRequestDto));
    }



    @GetMapping("/api/member/{oauth}/callback")
    public ResponseEntity<OauthResponseModel> OauthLogin(@RequestParam(name = "code") String code, HttpServletResponse response,
                                                         @RequestParam(value = "state", required = false) String state , @PathVariable String oauth) throws JsonProcessingException {
        if(oauth.equals("google"))
            return googleService.oauthLogin(code, response);
//        else if (oauth.equals("kakao")) {
//            return googleService.kakaologin(code, response);
//        }

        OauthResponseModel oauthResponseModel = OauthResponseModel.builder()
                .code(HttpStatus.OK.value())
                .httpStatus(HttpStatus.OK)
                .message("해당하는 소셜 로그인 정보가 없음").build();
        //////수정
        return new ResponseEntity<>(oauthResponseModel, oauthResponseModel.getHttpStatus());
    }

    @PostMapping("members/mailConfirms")
    String mailConfirm(@RequestParam("email") String email)throws Exception{

        String code=registerMail.sendSimpleMessage(email);
        System.out.println("인증코드 : "+code);
        return code;
    }


}