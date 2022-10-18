package com.finalproject.everrent_be.domain.chat.repository;


import com.finalproject.everrent_be.domain.chat.dto.ChatMessageDto;
import com.finalproject.everrent_be.domain.chat.model.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@RequiredArgsConstructor
@Repository
public class ChatMessageRepository {

    private final ChatMessageJpaRepository chatMessageJpaRepository;

    @Transactional
    public ChatMessageDto save(ChatMessageDto chatMessageDto) {
        String roomId = chatMessageDto.getRoomId();
        return chatMessageDto;
    }

    //채팅 리스트 가져오기
    @Transactional
    public List<ChatMessageDto> findAllMessage(String roomId) {
        List<ChatMessageDto> chatMessageDtoList = new ArrayList<>();
        List<ChatMessage> chatMessages = chatMessageJpaRepository.findAllByRoomId(roomId);

        for (ChatMessage chatMessage : chatMessages) {
            LocalDateTime createdAt = chatMessage.getCreatedAt();
            String createdAtString = createdAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.KOREA));
            ChatMessageDto chatMessageDto = new ChatMessageDto(chatMessage,createdAtString);
            chatMessageDtoList.add(chatMessageDto);
        }
        return chatMessageDtoList;
    }
}