package com.finalproject.everrent_be.domain.chat.dto;

import com.finalproject.everrent_be.domain.chat.model.Chat;
import com.finalproject.everrent_be.domain.member.model.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChatDto {
    private String content;
    private Long memberId;
    private String createdAt;

    private Long roomInfoId;

    private Long chaId;
//    private String proPic;


    public ChatDto (Chat chat) {
        this.chaId=chat.getId();
        Member member = chat.getRoomDetail().getMember();
        this.memberId = member.getId();
        this.content = chat.getMessage();
        this.roomInfoId = chat.getRoomInfoId();
        this.createdAt = chat.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").withZone(ZoneId.of("Asia/Seoul")));
    }
}