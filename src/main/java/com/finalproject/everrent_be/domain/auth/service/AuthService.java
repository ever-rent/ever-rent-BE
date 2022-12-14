package com.finalproject.everrent_be.domain.auth.service;



import com.finalproject.everrent_be.domain.auth.dto.EmailCheckRequestDto;
import com.finalproject.everrent_be.domain.auth.dto.LoginRequestDto;
import com.finalproject.everrent_be.domain.imageupload.service.AWSS3UploadService;
import com.finalproject.everrent_be.domain.imageupload.service.FileUploadService;
import com.finalproject.everrent_be.domain.member.service.MemberService;
import com.finalproject.everrent_be.domain.token.dto.TokenDto;
import com.finalproject.everrent_be.domain.token.dto.TokenRequestDto;
import com.finalproject.everrent_be.global.common.ResponseDto;
import com.finalproject.everrent_be.global.error.ErrorCode;
import com.finalproject.everrent_be.global.jwt.TokenProvider;
import com.finalproject.everrent_be.domain.member.dto.MemberCheckRequestDto;
import com.finalproject.everrent_be.domain.member.dto.MemberRequestDto;
import com.finalproject.everrent_be.domain.member.dto.MemberResponseDto;
import com.finalproject.everrent_be.domain.member.model.Member;
import com.finalproject.everrent_be.domain.token.model.RefreshToken;
import com.finalproject.everrent_be.domain.member.repository.MemberRepository;
import com.finalproject.everrent_be.domain.token.model.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final MemberService memberService;
    private final RefreshTokenRepository refreshTokenRepository;

    public final FileUploadService fileUploadService;




    public boolean nicknameCheck(MemberCheckRequestDto checkRequestDto)
    {
        if(memberRepository.existsByMemberName(checkRequestDto.getMemberName()))
        {
            return false;
        }
        return true;
    }

    public boolean emailCheck(String email)
    {
        if(memberRepository.existsByEmail(email))
        {
            return false;
        }
        return true;
    }

    @Transactional
    public ResponseDto signup(MemberRequestDto memberRequestDto) {


        if(memberRepository.existsByMemberName(memberRequestDto.getMemberName()))
        {
            return ResponseDto.is_Fail(ErrorCode.DUPLICATE_NICKNAME);
        }
        if(memberRepository.existsByEmail(memberRequestDto.getEmail()))
        {
            return ResponseDto.is_Fail(ErrorCode.DUPLICATE_EMAIL);
        }
        if(!(Pattern.matches("[a-zA-Z0-9???-??????-??????-???]*$",memberRequestDto.getMemberName()) && (memberRequestDto.getMemberName().length() > 1 && memberRequestDto.getMemberName().length() <15)
                && Pattern.matches("[a-zA-Z0-9]*$",memberRequestDto.getPassword()) && (memberRequestDto.getPassword().length() > 7 && memberRequestDto.getPassword().length() <33))){

            throw new IllegalArgumentException("????????? ?????? ???????????? ????????? ??????????????????.");
        }

        Member member = memberRequestDto.toMember(passwordEncoder);
        memberRepository.save(member);

        MemberResponseDto memberResponseDto=new MemberResponseDto(member);


        return ResponseDto.is_Success(memberResponseDto);
    }
    @Transactional
    public ResponseDto updateMyInfo(MemberRequestDto memberRequestDto) {

        Member member=memberService.getMemberfromContext();
        if(memberRequestDto.getPassword().equals("????????????")){
            if(!(Pattern.matches("[a-zA-Z0-9???-??????-??????-???]*$",memberRequestDto.getMemberName()) && (memberRequestDto.getMemberName().length() > 1 && memberRequestDto.getMemberName().length() <15))){
                throw new IllegalArgumentException("????????? ????????? ??????????????????.");
            }
            member.notPWupdate(memberRequestDto);

        }else{
        if(!(Pattern.matches("[a-zA-Z0-9???-??????-??????-???]*$",memberRequestDto.getMemberName()) && (memberRequestDto.getMemberName().length() > 1 && memberRequestDto.getMemberName().length() <15)
                && Pattern.matches("[a-zA-Z0-9]*$",memberRequestDto.getPassword()) && (memberRequestDto.getPassword().length() > 7 && memberRequestDto.getPassword().length() <33))){

            throw new IllegalArgumentException("????????? ?????? ???????????? ????????? ??????????????????.");
        }
        member.update(memberRequestDto,passwordEncoder);}
        MemberResponseDto memberResponseDto=new MemberResponseDto(member);
        return ResponseDto.is_Success(memberResponseDto);
    }



    @Transactional
    public TokenDto login(LoginRequestDto loginRequestDto) {
//        if (!memberRepository.existsByNickname(memberRequestDto.getNickname()) ||
//                !memberRepository.existsByPassword(passwordEncoder.encode(memberRequestDto.getPassword()))) {
//            throw new RuntimeException("???????????? ?????? ??? ????????????");
//        }
        // 1. Login ID/PW ??? ???????????? AuthenticationToken ??????
        UsernamePasswordAuthenticationToken authenticationToken = loginRequestDto.toAuthentication();
        // 2. ????????? ?????? (????????? ???????????? ??????) ??? ??????????????? ??????
        //    authenticate ???????????? ????????? ??? ??? CustomUserDetailsService ?????? ???????????? loadUserByUsername ???????????? ?????????
        /*    AuthService?????? AuthenticationManagerBuilder ?????? ??????
              AuthenticationManagerBuilder ?????? AuthenticationManager ??? ????????? ProviderManager ??????
              org.springframework.security.authentication.ProviderManager ??? AbstractUserDetailsAuthenticationProvider ??? ?????? ???????????? DaoAuthenticationProvider ??? ??????????????? ??????
              DaoAuthenticationProvider ??? authenticate ????????? retrieveUser ??? DB ??? ?????? ????????? ????????? ???????????? additionalAuthenticationChecks ??? ???????????? ??????
              retrieveUser ???????????? UserDetailsService ?????????????????? ?????? ????????? CustomUserDetailsService ???????????? ??????????????? ???????????? loadUserByUsername ??? ?????????*/

        try{
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

            // 3. ?????? ????????? ???????????? JWT ?????? ??????
            TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);
            // 4. RefreshToken ??????
            RefreshToken refreshToken = RefreshToken.builder()
                    .key(authentication.getName())
                    .value(tokenDto.getRefreshToken())
                    .build();
            refreshTokenRepository.save(refreshToken);
            // 5. ?????? ??????
            return tokenDto;
        } catch (Exception e){
            throw new IllegalArgumentException("???????????? ?????? ??? ????????????.");
        }
    }

    public ResponseDto<?> getOtherInfo(String memberId) {
        Member member=memberRepository.findById(Long.valueOf(memberId)).orElseThrow(
                () -> new IllegalArgumentException("?????? ???????????? ???????????? ????????????.")
        );
        MemberResponseDto memberResponseDto=new MemberResponseDto(member);

        return ResponseDto.is_Success(memberResponseDto);

    }

    @Transactional
    public ResponseDto<?> pwChange(LoginRequestDto loginRequestDto)
    {
        if(!Pattern.matches("[a-zA-Z0-9]*$",loginRequestDto.getPassword()) && (loginRequestDto.getPassword().length() > 7 && loginRequestDto.getPassword().length() <33)){

            throw new IllegalArgumentException("???????????? ????????? ??????????????????.");
        }

        Member member=memberRepository.findMemberByEmail(loginRequestDto.getEmail());
        member.pwUpdate(loginRequestDto.getPassword(),passwordEncoder);

        return ResponseDto.is_Success("?????? ??????");


    }

    @Transactional
    public ResponseDto<?> deleteMember()
    {
        Member member=memberService.getMemberfromContext();
        memberRepository.delete(member);
        return ResponseDto.is_Success("????????? ?????????????????????.");
    }

    public void postImage(MultipartFile multipartFile) {
        String imgUrl= fileUploadService.uploadImage(multipartFile);
        Member member= memberService.getMemberfromContext();
        member.imgUpdate(imgUrl);
        memberRepository.save(member);
    }
}