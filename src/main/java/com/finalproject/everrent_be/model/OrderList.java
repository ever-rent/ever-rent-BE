package com.finalproject.everrent_be.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@NoArgsConstructor
@Getter
@Entity
@Builder
@AllArgsConstructor
public class OrderList extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @JoinColumn(name = "member_id", nullable = false) //구매자
    @ManyToOne(fetch = FetchType.LAZY) //LAZY: 참조 객체들의 데이터들은 무시하고 해당 엔티티의 데이터만을 가져옴
    private Member member;

    @JoinColumn(name="product_id",nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    @Column(nullable = false)
    private LocalDate buyStart;

    @Column(nullable = false)
    private LocalDate buyEnd;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;


    public OrderList(Member member,Product product,LocalDate buyStart,LocalDate buyEnd,Status status){
        this.member=member;
        this.product=product;
        this.buyStart=buyStart;
        this.buyEnd=buyEnd;
        this.status=status;
    };
    public void updateStatus(Status status)
    {
        this.status=status;

    }





}
