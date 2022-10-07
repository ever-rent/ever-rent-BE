package com.finalproject.everrent_be.domain.product.model;

import com.finalproject.everrent_be.domain.order.model.OrderList;
import com.finalproject.everrent_be.domain.product.dto.ProductRequestDto;
import com.finalproject.everrent_be.domain.member.model.Member;
import com.finalproject.everrent_be.domain.wishlist.model.WishList;
import com.finalproject.everrent_be.global.common.Status;
import com.finalproject.everrent_be.global.common.Timestamped;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

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

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderList> orderLists;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String cateId;
    @Column(nullable = false)
    private String imgUrl;

    @Column(nullable = false)
    private LocalDate rentStart;

    @Column(nullable = false)
    private LocalDate rentEnd;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private String mapLocation;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @Column(nullable = false)
    private int wishNum;


    public Product(ProductRequestDto productRequestDto,Member member,String imageUrl,LocalDate rentStart,LocalDate rentEnd){
        this.productName=productRequestDto.getProductName();
        this.price=productRequestDto.getPrice();
        this.content=productRequestDto.getContent();
        this.imgUrl=imageUrl;
        this.cateId=productRequestDto.getCateId();
        this.rentStart=rentStart;
        this.rentEnd=rentEnd;
        this.location=productRequestDto.getLocation();
        this.mapLocation=productRequestDto.getMapLocation();
        this.status=Status.WAITING;
        this.member=member;
        this.wishNum=0;
    }
    public void update(ProductRequestDto productRequestDto,Member member, String imgUrl,LocalDate rentStart,LocalDate rentEnd){
        this.productName=productRequestDto.getProductName();
        this.price=productRequestDto.getPrice();
        this.content=productRequestDto.getContent();
        this.imgUrl=imgUrl;
        this.cateId=productRequestDto.getCateId();
        this.rentStart=rentStart;
        this.rentEnd=rentEnd;
        this.location=productRequestDto.getLocation();
        this.mapLocation=productRequestDto.getMapLocation();
        this.status=Status.WAITING;
        this.member=member;
    }
    public void updateStatus(Status status)
    {
        this.status=status;
    }

    public void updateWish(int num){
        this.wishNum+=num;
    }


}
