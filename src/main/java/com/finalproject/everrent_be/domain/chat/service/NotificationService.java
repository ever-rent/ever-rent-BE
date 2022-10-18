package com.finalproject.everrent_be.domain.chat.service;


import com.finalproject.everrent_be.domain.chat.dto.NotificationDto;
import com.finalproject.everrent_be.domain.chat.model.ChatMessage;
import com.finalproject.everrent_be.domain.chat.model.InvitedMembers;
import com.finalproject.everrent_be.domain.chat.repository.ChatMessageJpaRepository;
import com.finalproject.everrent_be.domain.chat.repository.ChatRoomJpaRepository;
import com.finalproject.everrent_be.domain.chat.repository.InvitedMembersRepository;
import com.finalproject.everrent_be.domain.member.model.Member;
import com.finalproject.everrent_be.domain.member.service.MemberService;
import com.finalproject.everrent_be.global.util.Check;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final InvitedMembersRepository invitedMembersRepository;
    private final ChatMessageJpaRepository chatMessageJpaRepository;
    private final ChatRoomJpaRepository chatRoomJpaRepository;
    private final Check check;
    private final MemberService memberService;

    @Transactional
    public List<NotificationDto> getNotification() {
        Member member=memberService.getMemberfromContext();

        Long memberId = member.getId();
        Boolean readCheck = false;

        List<NotificationDto> notificationDtoList = new ArrayList<>();
        List<InvitedMembers> invitedMembers = invitedMembersRepository.findAllByMemberIdAndReadCheck(memberId, readCheck);

        for (InvitedMembers invitedMember : invitedMembers) {
            List<ChatMessage> findChatMessageDtoList = chatMessageJpaRepository.findAllByRoomId(invitedMember.getRoomId());
            for (ChatMessage findChatMessageDto : findChatMessageDtoList) {
                NotificationDto notificationDto = new NotificationDto();
                if (findChatMessageDto.getMessage().isEmpty()) {
                    notificationDto.setMessage("메세지가 없어요😲");
                } else {
                    notificationDto.setMessage(findChatMessageDto.getMessage());
                }
                notificationDto.setMemberName(findChatMessageDto.getSender());
                notificationDto.setRoomId(findChatMessageDto.getRoomId());
                notificationDtoList.add(notificationDto);
            }
        }
        return notificationDtoList;
    }

}
