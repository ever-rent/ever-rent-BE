package com.finalproject.everrent_be.domain.chat.model;


import com.finalproject.everrent_be.domain.member.model.Member;
import com.finalproject.everrent_be.domain.product.model.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.concurrent.ThreadLocalRandom;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class ChatRoom implements Serializable {
    private static final long serialVersionUID = 6494678977089006639L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(nullable = false)
    @GeneratedValue
    private String roomId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    //채팅방 생성
    public static ChatRoom create(Product product, Member member) {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.roomId = String.valueOf(ThreadLocalRandom.current().nextInt(100000, 1000000));
        chatRoom.member = member;
        chatRoom.product = product;
        return chatRoom;
    }
}