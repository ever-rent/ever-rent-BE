package com.finalproject.everrent_be.model;

import com.finalproject.everrent_be.dto.ProductRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Entity
@Builder
@AllArgsConstructor
public class Product extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String productName;

    @Column(nullable = false)
    private String price;

    @JoinColumn(name = "member_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY) //LAZY: 참조 객체들의 데이터들은 무시하고 해당 엔티티의 데이터만을 가져옴
    private Member member;



    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String imgUrl;

    public void update(ProductRequestDto productRequestDto,Member member, String url){
        this.productName=productRequestDto.getProductName();
        this.price=productRequestDto.getPrice();
        this.content=productRequestDto.getContent();
        this.imgUrl=url;
        this.member=member;
    }

}
