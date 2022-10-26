package com.finalproject.everrent_be.domain.mypage.dto;

import com.finalproject.everrent_be.domain.member.model.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MypageResponseDto {

    private Long id;
    private String email;
    private String memberName;
    private String badges;
    private String mainAddress;
    private String subAddress;
    private String rating;
    private String imgUrl;
    public MypageResponseDto(Member member)
    {
        this.id= member.getId();
        this.email=member.getEmail();
        this.rating= member.getRating();
        this.memberName=member.getMemberName();
        this.badges=member.getBadges();
        this.mainAddress=member.getMainAddress();
        this.subAddress=member.getSubAddress();
        this.imgUrl=member.getImgUrl();
    }

}
