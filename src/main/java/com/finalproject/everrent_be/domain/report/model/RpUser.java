package com.finalproject.everrent_be.domain.report.model;

import com.finalproject.everrent_be.domain.member.model.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
@NoArgsConstructor
@Getter
@Entity
@Builder
@AllArgsConstructor
public class RpUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //신고대상
    @JoinColumn(name = "member_id", nullable = false) //신고대상자
    @ManyToOne(fetch = FetchType.LAZY) //LAZY: 참조 객체들의 데이터들은 무시하고 해당 엔티티의 데이터만을 가져옴
    private Member rpmember;
    //신고사유
    @Column(nullable = false)
    private String rpreason;


    //신고자 memberid
    private Long whoisRpId;

    public RpUser(Member rpMember, String rpReason, Long whoisRpId){
        this.rpmember=rpMember;
        this.rpreason=rpReason;
        this.whoisRpId=whoisRpId;
    }

}
