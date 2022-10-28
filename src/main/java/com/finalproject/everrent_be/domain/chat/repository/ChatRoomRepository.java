package com.finalproject.everrent_be.domain.chat.repository;


import com.finalproject.everrent_be.domain.chat.dto.ChatListMessageDto;
import com.finalproject.everrent_be.domain.chat.dto.ChatRoomResponseDto;
import com.finalproject.everrent_be.domain.chat.model.ChatMessage;
import com.finalproject.everrent_be.domain.chat.model.ChatRoom;
import com.finalproject.everrent_be.domain.chat.model.InvitedMembers;
import com.finalproject.everrent_be.domain.member.model.Member;
import com.finalproject.everrent_be.domain.member.service.MemberService;
import com.finalproject.everrent_be.domain.product.model.Product;
import com.finalproject.everrent_be.global.util.Check;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@RequiredArgsConstructor
@Repository
public class ChatRoomRepository {
    private final ChatRoomJpaRepository chatRoomJpaRepository;
    private final InvitedMembersRepository invitedMembersRepository;
    private final ChatMessageJpaRepository chatMessageJpaRepository;
    private final MemberService memberService;

    //내가 참여한 모든 채팅방 목록 조회
    @Transactional
    public ChatListMessageDto findAllRoom() {
        Member member = memberService.getMemberfromContext();

        List<InvitedMembers> invitedMembers = invitedMembersRepository.findAllByMemberId(member.getId());
        if (invitedMembers.isEmpty()) { throw new IllegalArgumentException(); }
        List<ChatRoomResponseDto> chatRoomResponseDtoList = new ArrayList<>();
        for (InvitedMembers invitedMember : invitedMembers) {
            if (invitedMember.getReadCheck()) {
                invitedMember.setReadCheck(false);
                invitedMember.setReadCheckTime(LocalDateTime.now());
            }
            //채팅방 있는지 확인
            ChatRoom chatRoom = chatRoomJpaRepository.findByRoomId(invitedMember.getRoomId());
            ChatMessage lastMessage = chatMessageJpaRepository.findTop1ByRoomIdOrderByCreatedAtDesc(invitedMember.getRoomId());
            ChatMessage lastTalkMessage = chatMessageJpaRepository.findTop1ByRoomIdAndTypeOrderByCreatedAtDesc(invitedMember.getRoomId(), ChatMessage.MessageType.TALK);
            ChatRoomResponseDto chatRoomResponseDto = new ChatRoomResponseDto();
            if (lastMessage.getMessage().isEmpty()) {
                chatRoomResponseDto.setLastMessage(lastTalkMessage.getMessage());
            } else {
                chatRoomResponseDto.setLastMessage(lastTalkMessage.getMessage());
            }
            LocalDateTime createdAt = lastMessage.getCreatedAt();
            String createdAtString = createdAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.KOREA));
            List<InvitedMembers> twoInvitedMembers = invitedMembersRepository.findAllByRoomId(chatRoom.getRoomId());
            for (InvitedMembers otherMember : twoInvitedMembers) {
                if (!otherMember.getMember().equals(member)) {
                    String otherMembername = otherMember.getMember().getMemberName();
                    String otherMemberProfileUrl = otherMember.getMember().getImgUrl();
                    chatRoomResponseDto.setOtherMemberName(otherMembername);
                    chatRoomResponseDto.setProfileUrl(otherMemberProfileUrl);
                }
            }
            String s=chatRoom.getProduct().getImgUrl();
            String[] imgUrlArray=s.split(" ");
            chatRoomResponseDto.setLastMessageTime(createdAtString);
            chatRoomResponseDto.setMemberName(member.getMemberName());
            chatRoomResponseDto.setProductId(chatRoom.getProduct().getId());
            chatRoomResponseDto.setProductImgUrl(imgUrlArray[0]);
            chatRoomResponseDto.setRoomId(chatRoom.getRoomId());
            chatRoomResponseDtoList.add(chatRoomResponseDto);
        }
        return new ChatListMessageDto(chatRoomResponseDtoList);
    }

    public void createChatRoom(ChatRoom chatRoom) {
        chatRoomJpaRepository.save(chatRoom); // DB 저장
    }
}