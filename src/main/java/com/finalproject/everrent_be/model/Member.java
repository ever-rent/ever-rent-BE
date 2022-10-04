package com.finalproject.everrent_be.model;


import com.fasterxml.jackson.annotation.JsonIgnore;

import com.finalproject.everrent_be.oauth.dto.GoogleUser;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

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
    @Column
    private String mainAddress;
    @Column
    private String subAddress;
    @Column
    private boolean policy;

    @Enumerated(EnumType.STRING)
    private Authority authority;


    public Member(String memberName, String password, String email, String mainAddress,String subAddress,
                   boolean policy) {

        this.memberName = memberName;

        this.password = password;
        this.email = email;
        this.mainAddress = mainAddress;
        this.subAddress=subAddress;
        this.policy = policy;
    }


    private String provider;// oauth2를 이용할 경우 어떤 플랫폼을 이용하는지
    //private String providerId;// oauth2를 이용할 경우 아이디값

    public Member(GoogleUser googleUser)
    {
        this.memberName=googleUser.getName();
        this.email=googleUser.getEmail();
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
}