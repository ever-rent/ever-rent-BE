package com.finalproject.everrent_be.domain.report.model;

import com.finalproject.everrent_be.domain.imageupload.member.model.Member;
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
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "member_id", nullable = false) //신고한 판매자
    @ManyToOne(fetch = FetchType.LAZY) //LAZY: 참조 객체들의 데이터들은 무시하고 해당 엔티티의 데이터만을 가져옴
    private Member member;

    //신고사유
    @Column(nullable = false)
    private String rtreason;





}
