package com.finalproject.everrent_be.domain.oauth.dto;


import com.finalproject.everrent_be.domain.imageupload.member.model.Member;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OAuthResponseDto {
    private String email;
    private String memberId;
    private String access_Token;
    private String refresh_Token;

    public OAuthResponseDto(Member member, String access_Token, String refresh_Token){
        this.email = member.getEmail();
        this.memberId = member.getMemberName();
        this.access_Token = access_Token;
        this.refresh_Token = refresh_Token;
    }
}