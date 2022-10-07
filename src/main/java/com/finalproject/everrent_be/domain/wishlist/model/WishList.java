package com.finalproject.everrent_be.domain.wishlist.model;

import com.finalproject.everrent_be.domain.member.model.Member;
import com.finalproject.everrent_be.domain.product.model.Product;
import com.finalproject.everrent_be.global.common.Timestamped;
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
public class WishList{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "member_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY) //LAZY: 참조 객체들의 데이터들은 무시하고 해당 엔티티의 데이터만을 가져옴
    private Member member;

    @JoinColumn(name = "product_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;



}
