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



    public boolean nicknameCheck(MemberCheckRequestDto checkRequestDto)
    {
        if(memberRepository.existsByMemberName(checkRequestDto.getMemberName()))
        {
            return false;
        }
        return true;
    }

    public boolean emailCheck(EmailCheckRequestDto checkRequestDto)
    {
        if(memberRepository.existsByEmail(checkRequestDto.getEmail()))
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
        if(!(Pattern.matches("[a-zA-Z0-9ㄱ-ㅎㅏ-ㅣ가-힣]*$",memberRequestDto.getMemberName()) && (memberRequestDto.getMemberName().length() > 1 && memberRequestDto.getMemberName().length() <15)
                && Pattern.matches("[a-zA-Z0-9]*$",memberRequestDto.getPassword()) && (memberRequestDto.getPassword().length() > 7 && memberRequestDto.getPassword().length() <33))){

            throw new IllegalArgumentException("닉네임 혹은 비밀번호 조건을 확인해주세요.");
        }

        Member member = memberRequestDto.toMember(passwordEncoder);
        memberRepository.save(member);

        MemberResponseDto memberResponseDto=new MemberResponseDto(member);


        return ResponseDto.is_Success(memberResponseDto);
    }
    @Transactional
    public ResponseDto updateMyInfo(MemberRequestDto memberRequestDto) {

        Member member=memberService.getMemberfromContext();

        if(!(Pattern.matches("[a-zA-Z0-9ㄱ-ㅎㅏ-ㅣ가-힣]*$",memberRequestDto.getMemberName()) && (memberRequestDto.getMemberName().length() > 1 && memberRequestDto.getMemberName().length() <15)
                && Pattern.matches("[a-zA-Z0-9]*$",memberRequestDto.getPassword()) && (memberRequestDto.getPassword().length() > 7 && memberRequestDto.getPassword().length() <33))){

            throw new IllegalArgumentException("닉네임 혹은 비밀번호 조건을 확인해주세요.");
        }

        member.update(memberRequestDto,passwordEncoder);

        MemberResponseDto memberResponseDto=new MemberResponseDto(member);


        return ResponseDto.is_Success(memberResponseDto);
    }
    @Transactional
    public TokenDto login(LoginRequestDto loginRequestDto) {
//        if (!memberRepository.existsByNickname(memberRequestDto.getNickname()) ||
//                !memberRepository.existsByPassword(passwordEncoder.encode(memberRequestDto.getPassword()))) {
//            throw new RuntimeException("사용자를 찾을 수 없습니다");
//        }
        // 1. Login ID/PW 를 기반으로 AuthenticationToken 생성
        UsernamePasswordAuthenticationToken authenticationToken = loginRequestDto.toAuthentication();
        // 2. 실제로 검증 (사용자 비밀번호 체크) 이 이루어지는 부분
        //    authenticate 메서드가 실행이 될 때 CustomUserDetailsService 에서 만들었던 loadUserByUsername 메서드가 실행됨
        /*    AuthService에서 AuthenticationManagerBuilder 주입 받음
              AuthenticationManagerBuilder 에서 AuthenticationManager 를 구현한 ProviderManager 생성
              org.springframework.security.authentication.ProviderManager 는 AbstractUserDetailsAuthenticationProvider 의 자식 클래스인 DaoAuthenticationProvider 를 주입받아서 호출
              DaoAuthenticationProvider 의 authenticate 에서는 retrieveUser 로 DB 에 있는 사용자 정보를 가져오고 additionalAuthenticationChecks 로 비밀번호 비교
              retrieveUser 내부에서 UserDetailsService 인터페이스를 직접 구현한 CustomUserDetailsService 클래스의 오버라이드 메소드인 loadUserByUsername 가 호출됨*/

        try{
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

            // 3. 인증 정보를 기반으로 JWT 토큰 생성
            TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);
            // 4. RefreshToken 저장
            RefreshToken refreshToken = RefreshToken.builder()
                    .key(authentication.getName())
                    .value(tokenDto.getRefreshToken())
                    .build();
            refreshTokenRepository.save(refreshToken);
            // 5. 토큰 발급
            return tokenDto;
        } catch (Exception e){
            throw new IllegalArgumentException("사용자를 찾을 수 없습니다.");
        }
    }

}