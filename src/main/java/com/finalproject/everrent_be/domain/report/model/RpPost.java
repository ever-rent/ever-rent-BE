package com.finalproject.everrent_be.domain.report.model;

import com.finalproject.everrent_be.domain.product.model.Product;
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
public class RpPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //신고대상
    @JoinColumn(name="product_id",nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Product rpproduct;
    //신고사유
    @Column(nullable = false)
    private String rpreason;


    //신고자 memberid
    private Long whoisRpId;

    public RpPost(Product rpproduct, String rpreason, Long whoisRpId){
        this.rpproduct=rpproduct;
        this.rpreason=rpreason;
        this.whoisRpId=whoisRpId;
    }





}
