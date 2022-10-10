package com.finalproject.everrent_be.domain.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RoomInviteDto {
    private Long memberId;
    private Long productId;
    private String title;
    private String memberName;
}
