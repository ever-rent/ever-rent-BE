package com.finalproject.everrent_be.dto;

import com.finalproject.everrent_be.model.Member;

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
    private String password;

    private String address;
    private boolean policy;

    public MemberResponseDto(Member member)
    {
        this.id=member.getId();
        this.memberName=member.getMemberName();
        this.email=member.getEmail();
        this.address=member.getAddress();
        this.policy= member.isPolicy();
    }
    public MemberResponseDto(String memberName){
        this.memberName=memberName;
    }


    public static MemberResponseDto of(Member member) {
        return new MemberResponseDto(member.getMemberName());
    }
}
