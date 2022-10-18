package com.finalproject.everrent_be.domain.chat.dto;


import lombok.*;

@Setter
@Getter
@NoArgsConstructor
public class ChatRoomResponseDto {
    private String lastMessage;

    private String MemberName;

    private String otherMemberName;

    private String profileUrl;

    private String lastMessageTime;

    private Long productId;

    private String productImgUrl;

    private String roomId;
}