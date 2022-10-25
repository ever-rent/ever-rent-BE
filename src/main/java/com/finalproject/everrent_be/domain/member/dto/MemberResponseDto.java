package com.finalproject.everrent_be.domain.member.dto;

import com.finalproject.everrent_be.domain.member.model.Member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberResponseDto {

    private Long id;

    private String email;
    private String memberName;
    //private String password;
    private String[] badges;
    private String imgUrl;
    private String mainAddress;
    private String subAddress;
    private boolean policy;

    private String rating;

    public MemberResponseDto(Member member)
    {
        this.id=member.getId();
        this.memberName=member.getMemberName();
        this.badges=member.getBadges().split("");
        this.email=member.getEmail();
        this.imgUrl=member.getImgUrl();
        this.mainAddress=member.getMainAddress();
        this.subAddress=member.getSubAddress();
        this.policy= member.isPolicy();
        this.rating=member.getRating();
    }
    public MemberResponseDto(String memberName){
        this.memberName=memberName;
    }


    public static MemberResponseDto of(Member member) {
        return new MemberResponseDto(member.getMemberName());
    }


}
