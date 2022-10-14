package com.finalproject.everrent_be.domain.chat.dto;


import com.finalproject.everrent_be.domain.member.model.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MemberDto {
    private String password;

    private String memberName;

    private String profileUrl;

    private Long id;

    public MemberDto(Member member) {
        this.password = member.getPassword();
        this.memberName = member.getMemberName();
        this.profileUrl = member.getImgUrl();
        this.id = member.getId();
    }
}