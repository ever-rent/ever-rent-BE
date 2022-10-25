package com.finalproject.everrent_be.domain.member.model;


import com.fasterxml.jackson.annotation.JsonIgnore;

import com.finalproject.everrent_be.domain.auth.dto.LoginRequestDto;
import com.finalproject.everrent_be.domain.member.dto.MemberRequestDto;
import com.finalproject.everrent_be.domain.order.model.OrderList;
import com.finalproject.everrent_be.domain.product.model.Product;
import com.finalproject.everrent_be.domain.wishlist.model.WishList;
import com.finalproject.everrent_be.global.common.Timestamped;
import com.finalproject.everrent_be.domain.oauth.dto.GoogleUser;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Entity
@Builder
@AllArgsConstructor
public class Member extends Timestamped {

    /*@Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(nullable = false, columnDefinition = "VARCHAR(36)")
    @Type(type = "uuid-char")
    private UUID uuid;*/

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String memberName;

    @JsonIgnore
    @Column(nullable = false)
    private String password;

    @JsonIgnore
    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Product> products;

    @JsonIgnore
    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderList> orderLists;

    @JsonIgnore
    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WishList> wishLists;

    @Column
    private String imgUrl;

    @Column(nullable = false)
    private String rating; //String으로 변환시켜서 보내기 productResponse에서도
    @Column
    private String mainAddress;
    @Column
    private String subAddress;
    @Column
    private boolean policy;

    @Enumerated(EnumType.STRING)
    private Authority authority;

    @Column
    private String badges;

    public Member(String memberName, String password, String email, String mainAddress,String subAddress,
                   boolean policy) {

        this.memberName = memberName;

        this.password = password;
        this.email = email;
        this.mainAddress = mainAddress;
        this.subAddress=subAddress;
        this.policy = policy;
    }
    public void update(MemberRequestDto memberRequestDto, PasswordEncoder passwordEncoder)
    {
        this.memberName = memberRequestDto.getMemberName();
        this.password=passwordEncoder.encode(memberRequestDto.getPassword());
        //this.email = memberRequestDto.getEmail();
        this.mainAddress = memberRequestDto.getMainAddress();
        this.subAddress=memberRequestDto.getSubAddress();
        //this.imgUrl=memberRequestDto.getImgUrl();
    }

    //패스워드 변경 안하는 경우
    public void notPWupdate(MemberRequestDto memberRequestDto) {
        this.memberName = memberRequestDto.getMemberName();
        //this.password=passwordEncoder.encode(memberRequestDto.getPassword());
        //this.email = memberRequestDto.getEmail();
        this.mainAddress = memberRequestDto.getMainAddress();
        this.subAddress=memberRequestDto.getSubAddress();
        //this.imgUrl=memberRequestDto.getImgUrl();
    }

    public void pwUpdate(String password, PasswordEncoder passwordEncoder)
    {
        this.password=passwordEncoder.encode(password);
    }

    private String provider;// oauth2를 이용할 경우 어떤 플랫폼을 이용하는지
    //private String providerId;// oauth2를 이용할 경우 아이디값

    public Member(GoogleUser googleUser)
    {
        this.memberName=googleUser.getName();
        this.email=googleUser.getEmail();
        this.rating="36.5";
        this.password="googlelogin";
        this.provider="Google";
    }

    @Builder(builderClassName = "OAuth2Register", builderMethodName = "oauth2Register")
    public Member(String memberId, String password, String email, String provider, String providerId) {
        this.memberName = memberId;
        this.password = password;
        this.email = email;
        this.provider = provider;

    }
    public void updateRating(Long num)
    {
        this.rating=String.valueOf(Double.valueOf(this.rating)-0.3+(num*0.1f));

    }

    public void imgUpdate(String imgUrl) {
        this.imgUrl=imgUrl;
    }

    public void setBadges(int index,String b){
        StringBuffer bf=new StringBuffer();
        bf.append(this.badges);
        //바꾸기
        bf.replace(index,index+1,b);//01000000000
        //다시 저장
        this.badges=bf.toString();
    }
}