package com.finalproject.everrent_be.domain.member.dto;

import com.finalproject.everrent_be.domain.member.model.Authority;
import com.finalproject.everrent_be.domain.member.model.Member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberRequestDto {


    private String email;
    private String memberName;

    private String password;

    private String mainAddress;

    private String subAddress;
    //private String imgUrl;


    public Member toMember(PasswordEncoder passwordEncoder) {
        return Member.builder()
                .memberName(memberName)
                .password(passwordEncoder.encode(password))
                .email(email)
                .mainAddress(mainAddress)
                .subAddress(subAddress)
                .rating("36.5")
                .authority(Authority.ROLE_USER)
                .badges("000000000")
                .build();
    }


    public UsernamePasswordAuthenticationToken toAuthentication() {
        return new UsernamePasswordAuthenticationToken(email, password);
    }
}