package com.finalproject.everrent_be.domain.chat.controller;

import com.finalproject.everrent_be.domain.chat.dto.ChatDto;
import com.finalproject.everrent_be.domain.chat.model.RoomDetail;
import com.finalproject.everrent_be.domain.chat.repository.RoomDetailRepository;
import com.finalproject.everrent_be.domain.chat.service.ChatService;
import com.finalproject.everrent_be.domain.member.model.Member;
import com.finalproject.everrent_be.domain.member.service.MemberService;
import com.finalproject.everrent_be.global.util.UserDetailsImpl;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Log4j2
@RestController
@AllArgsConstructor
public class ChatController {
    private final RoomDetailRepository roomDetailRepository;
    public final MemberService memberService;

    private final ChatService chatService;
    /*채팅방과 연결*/
    @GetMapping("/room/{productId}")
    public ResponseEntity<?> getRoomChat(@PathVariable String productId) {
        Member member = memberService.getMemberfromContext();
        RoomDetail roomDetail = roomDetailRepository.findByMember_IdAndProduct_Id(member.getId(), Long.valueOf(productId))
                .orElseThrow();
        List<ChatDto> chats = chatService.getChat(roomDetail.getRoomInfo());
        return ResponseEntity.ok().body(chats);
    }
}