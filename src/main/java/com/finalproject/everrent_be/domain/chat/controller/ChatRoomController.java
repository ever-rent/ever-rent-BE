package com.finalproject.everrent_be.domain.chat.controller;


import com.finalproject.everrent_be.domain.chat.dto.ChatListMessageDto;
import com.finalproject.everrent_be.domain.chat.dto.ChatResponseDto;
import com.finalproject.everrent_be.domain.chat.repository.ChatRoomRepository;
import com.finalproject.everrent_be.domain.chat.service.ChatRoomService;
import com.finalproject.everrent_be.global.common.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RestController
public class ChatRoomController {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomService chatRoomService;

    // 내 채팅방 목록 반환
    @GetMapping("/chat/rooms")
    public ChatListMessageDto room() {

        return chatRoomRepository.findAllRoom();
    }

    // 특정 채팅방 입장
    @PostMapping("/chat/room/{productId}/{roomId}")
    public ResponseDto<?> roomInfo(@PathVariable String productId, @PathVariable String roomId) {

        return  ResponseDto.is_Success(new ChatResponseDto(roomId, productId));
    }

    //특정 채팅방 생성
    @PostMapping("/create/chat/{productId}")
    public ResponseDto<?> chatRoomCreate(@PathVariable String productId) {
        return chatRoomService.createChatRoom(productId);
    }

}
