package com.finalproject.everrent_be.domain.oauth.service;



import com.finalproject.everrent_be.domain.imageupload.member.dto.MemberRequestDto;
import com.finalproject.everrent_be.domain.oauth.dto.GoogleUser;
import com.finalproject.everrent_be.domain.oauth.dto.OAuthResponseDto;
import com.finalproject.everrent_be.domain.oauth.dto.OAuthToken;
import com.finalproject.everrent_be.domain.oauth.model.OauthResponseModel;
import com.finalproject.everrent_be.domain.token.dto.TokenDto;
import com.finalproject.everrent_be.gloabl.jwt.TokenProvider;

import com.finalproject.everrent_be.domain.imageupload.member.model.Member;
import com.finalproject.everrent_be.domain.token.model.RefreshToken;
import com.finalproject.everrent_be.domain.imageupload.member.repository.MemberRepository;
import com.finalproject.everrent_be.domain.token.model.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GoogleService {
    private final TokenProvider TokenProvider;//////겹치는 기능 찾기
    private final OAuthService oAuthService;
    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    public ResponseEntity<OauthResponseModel> oauthLogin(String code, HttpServletResponse response) {

        // 이해하기 -------------------------------------------------------
        ResponseEntity<String> accessTokenResponse = oAuthService.createPostRequest(code);
        OAuthToken oAuthToken = oAuthService.getAccessToken(accessTokenResponse);

        ResponseEntity<String> userInfoResponse = oAuthService.createGetRequest(oAuthToken);
        GoogleUser googleUser = oAuthService.getUserInfo(userInfoResponse);

        // 이해하기 -------------------------------------------------------

        if (!isJoinedUser(googleUser)) {
            signUp(googleUser);
        }

        String[] st=googleUser.getEmail().split("@");
        String googleusername=st[0];
        System.out.println(googleusername);
        MemberRequestDto memberRequestDto= MemberRequestDto.builder()
                .memberName(googleusername)
                .email(googleUser.getEmail())
                .password("googlelogin")
                .build();

        UsernamePasswordAuthenticationToken authenticationToken = memberRequestDto.toAuthentication();

        try{
            //Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
            Member member=memberRepository.findMemberByEmail(googleUser.getEmail());
            TokenDto tokenDto = TokenProvider.generateTokenDto(authenticationToken);
            RefreshToken refreshToken = RefreshToken.builder()
                    .key(authenticationToken.getName())
                    .value(tokenDto.getRefreshToken())
                    .build();
            refreshTokenRepository.save(refreshToken);

            response.addHeader("Authentication", tokenDto.getAccessToken());
            response.addHeader("Refresh-Token", tokenDto.getRefreshToken());

            OAuthResponseDto authResponseDto = new OAuthResponseDto(member, tokenDto.getAccessToken(), tokenDto.getRefreshToken());
            List<OAuthResponseDto> list = new ArrayList<>();
            list.add(authResponseDto);

            OauthResponseModel responseModel = OauthResponseModel.builder()
                    .code(HttpStatus.OK.value())
                    .httpStatus(HttpStatus.OK)
                    .message("구글 로그인 완료")
                    .data(list).build();
            return new ResponseEntity<>(responseModel, responseModel.getHttpStatus());

        }catch (Exception e){
            throw new IllegalArgumentException("사용자를 찾을 수 없습니다.");
        }



    }

    private boolean isJoinedUser(GoogleUser googleUser) {
        Member member = memberRepository.findMemberByEmail(googleUser.getEmail());
        if (member != null)
            return true;
        else
            return false;
    }

    //구글로 회원가입 -> 구글 유저를 DB에 저장
    private void signUp(GoogleUser googleUser) {

        Member member = memberRepository.findMemberByEmail(googleUser.getEmail());
        if (member != null)
            System.out.println("이미 사용자가 존재합니다.");
        else {
            //유저 DB에 저장
            Member new_member = new Member(googleUser);
            memberRepository.save(new_member);

            //리프레시 토큰 생성 후 DB에 저장

        }

    }

}