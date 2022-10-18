package com.finalproject.everrent_be.domain.chat.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChatResponseDto {
    private String roomId;

    private String productId;

    public ChatResponseDto(String roomId, String productId) {
        this.roomId = roomId;
        this.productId = productId;
    }
}