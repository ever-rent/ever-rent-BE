package com.finalproject.everrent_be.domain.chat.model;

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
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoomDetail extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="room_info_id")
    private RoomInfo roomInfo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @OneToMany(mappedBy = "roomDetail", cascade = CascadeType.REMOVE ,fetch = FetchType.EAGER, orphanRemoval = true)
    private List<Chat> chats;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    public RoomDetail(RoomInfo roomInfo, Member member, Product product) {
        this.roomInfo = roomInfo;
        this.member = member;
        this.product = product;
    }

    @Column
    private Long chatId;

    public void updateChatId(Long chatId) {
        this.chatId = chatId;
    }
}
