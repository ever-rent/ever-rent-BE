package com.finalproject.everrent_be.domain.chat.service;

import com.finalproject.everrent_be.domain.chat.dto.ChatDto;
import com.finalproject.everrent_be.domain.chat.model.Chat;
import com.finalproject.everrent_be.domain.chat.model.RoomDetail;
import com.finalproject.everrent_be.domain.chat.model.RoomInfo;
import com.finalproject.everrent_be.domain.chat.repository.ChatRepository;
import com.finalproject.everrent_be.domain.chat.repository.RoomDetailRepository;
import com.finalproject.everrent_be.domain.chat.repository.RoomInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {
    private final RoomDetailRepository roomDetailsRepository;
    private final RoomInfoRepository roomInfoRepository;
    private final ChatRepository chatRepository;

    @Transactional
    public ChatDto saveChat(Long roomId, ChatDto message) {
        RoomDetail roomDetail = roomDetailsRepository.findByRoomInfo_IdAndMember_MemberId(roomId, message.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("채팅방에 관한 정보가 없습니다."));
        roomDetail.getRoomInfo().updateRecentChat(message.getContent());
        RoomInfo roomInfo = roomInfoRepository.findById(roomId).orElseThrow(()-> new IllegalArgumentException("채팅방이 존재하지 않습니다."));
        Chat chat = Chat.builder()
                .id(message.getChaId())
                .roomInfoId(roomInfo.getId())
                .roomDetail(roomDetail)
                .message(message.getContent())
                .build();

        ChatDto chatDto = new ChatDto(chatRepository.save(chat));
        return chatDto;
    }


    /*채팅 보내기*/
    @Transactional
    public List<ChatDto> getChat(RoomInfo roomInfo) {
        List<Chat> chats = chatRepository.findByRoomDetail_RoomInfo_IdOrderByCreatedAtDesc(roomInfo.getId());
        return chats.stream()
                .map(ChatDto::new)
                .collect(Collectors.toList());
    }
}