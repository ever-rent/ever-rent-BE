package com.finalproject.everrent_be.domain.chat.model;

import com.finalproject.everrent_be.domain.chat.model.RoomDetail;
import com.finalproject.everrent_be.domain.member.model.Member;
import com.finalproject.everrent_be.domain.product.model.Product;
import com.finalproject.everrent_be.global.common.Timestamped;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoomInfo extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "roomInfo", cascade = CascadeType.ALL,orphanRemoval = true)
    private List<RoomDetail> roomDetail;


    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String memberName;

    @Column
    private String recentChat;

    public void updateRecentChat(String recentChat) {
        this.recentChat = recentChat;
    }
}
