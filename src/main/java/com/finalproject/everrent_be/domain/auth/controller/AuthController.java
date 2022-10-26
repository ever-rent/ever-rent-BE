package com.finalproject.everrent_be.domain.auth.controller;



import com.fasterxml.jackson.core.JsonProcessingException;

import com.finalproject.everrent_be.domain.auth.dto.EmailCheckRequestDto;
import com.finalproject.everrent_be.domain.auth.dto.LoginRequestDto;
import com.finalproject.everrent_be.domain.member.dto.MemberCheckRequestDto;
import com.finalproject.everrent_be.domain.product.dto.ProductRequestDto;
import com.finalproject.everrent_be.domain.token.dto.TokenDto;
import com.finalproject.everrent_be.domain.token.dto.TokenRequestDto;
import com.finalproject.everrent_be.domain.auth.emailverified.service.RegisterMail;
import com.finalproject.everrent_be.global.common.ResponseDto;
import com.finalproject.everrent_be.domain.member.dto.MemberRequestDto;
import com.finalproject.everrent_be.domain.oauth.model.OauthResponseModel;
import com.finalproject.everrent_be.domain.oauth.service.GoogleService;
import com.finalproject.everrent_be.domain.member.repository.MemberRepository;
import com.finalproject.everrent_be.domain.auth.service.AuthService;
import com.finalproject.everrent_be.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;


//회원가입 로그인 로그아웃
@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final MemberRepository memberRepository;
    private final GoogleService googleService;
    private final RegisterMail registerMail;

    // 회원가입
    @PostMapping("/signups")
    public ResponseDto<?> signup(@RequestBody MemberRequestDto memberRequestDto) {
        return authService.signup(memberRequestDto);
    }

    //아이디 중복검사
    @PostMapping("/idchecks")
    public boolean idcheck(@RequestBody MemberCheckRequestDto checkRequestDto)
    {
        return authService.nicknameCheck(checkRequestDto);
    }

    // 이메일 중복검사
//    @PostMapping("/emailchecks")
//    public boolean emailCheck(@RequestBody EmailCheckRequestDto checkRequestDto)
//    {
//        return authService.emailCheck(checkRequestDto);
//    }

    //이미지 전송
    @PostMapping("/updateInfo/image")
    public void postImage(@RequestPart MultipartFile multipartFile){
        authService.postImage(multipartFile);
    }

    //회원정보 수정
    @PutMapping("/updateInfo")
    public ResponseDto<?> updateMyInfo(@RequestBody MemberRequestDto memberRequestDto)
    {
        return authService.updateMyInfo(memberRequestDto);
    }


    //다른회원정보 조회
    @GetMapping("/updateInfo/{memberId}")
    public ResponseDto<?> getOtherInfo(@PathVariable String memberId)
    {
        return authService.getOtherInfo(memberId);
    }

    // 로그인
    @PostMapping("/logins")
    public ResponseDto<?> login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {
        TokenDto tokenDto = authService.login(loginRequestDto);
        response.setHeader("Authorization", "Bearer " + tokenDto.getAccessToken());
        response.setHeader("Refresh-Token", tokenDto.getRefreshToken());
        response.setHeader("Access-Token-Expire-Time", String.valueOf(tokenDto.getAccessTokenExpiresIn()));
        ResponseDto responseDto = ResponseDto.is_Success(memberRepository.findMemberByEmail(loginRequestDto.getEmail()));
        return responseDto;
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

    @PostMapping("/mailConfirms")
    public ResponseDto<?> mailConfirm(@RequestParam("email") String email)throws Exception{
        if(authService.emailCheck(email))
        {
            String code=registerMail.sendSimpleMessage(email);
            System.out.println("인증코드 : "+code);
            return ResponseDto.is_Success(code);
        }
        else{
            return ResponseDto.is_Fail(ErrorCode.DUPLICATE_EMAIL);
        }
    }

    @PostMapping("/pwMailConfirms")
    public ResponseDto<?> mailConfirmPw(@RequestParam("email") String email)throws Exception{
        if(!authService.emailCheck(email))
        {
            String code=registerMail.sendSimpleMessage(email);
            System.out.println("인증코드 : "+code);
            return ResponseDto.is_Success(code);
        }
        else{
            return ResponseDto.is_Fail(ErrorCode.DUPLICATE_EMAIL);
        }
    }

    @PutMapping("/pwChanges")
    public ResponseDto<?> pwChange(@RequestBody LoginRequestDto loginRequestDto)
    {
        return authService.pwChange(loginRequestDto);
    }

    @DeleteMapping("/deleteMembers")
    public ResponseDto<?> deleteMember()
    {
        return authService.deleteMember();
    }


}