package com.finalproject.everrent_be.domain.chat.dto;

import com.finalproject.everrent_be.domain.chat.model.RoomDetail;
import lombok.*;

import java.time.format.DateTimeFormatter;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class RoomInfoResponseDto  {
    private Long roomInfoId;
    private String memberName;
    private String title;
    private String createdAt;


    public static RoomInfoResponseDto Info(RoomDetail roomDetail) {

        return RoomInfoResponseDto.builder()
                .roomInfoId(roomDetail.getRoomInfo().getId())
                .memberName(roomDetail.getRoomInfo().getMember().getMemberName())
                .title(roomDetail.getRoomInfo().getTitle())
                .createdAt(roomDetail.getRoomInfo().getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                .build();
    }
}