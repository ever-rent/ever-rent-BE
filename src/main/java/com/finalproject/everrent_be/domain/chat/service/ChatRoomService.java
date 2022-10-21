package com.finalproject.everrent_be.domain.chat.service;



import com.finalproject.everrent_be.domain.chat.model.ChatRoom;
import com.finalproject.everrent_be.domain.chat.model.InvitedMembers;
import com.finalproject.everrent_be.domain.chat.repository.ChatRoomJpaRepository;
import com.finalproject.everrent_be.domain.chat.repository.ChatRoomRepository;
import com.finalproject.everrent_be.domain.chat.repository.InvitedMembersRepository;
import com.finalproject.everrent_be.domain.member.model.Member;

import com.finalproject.everrent_be.domain.member.service.MemberService;
import com.finalproject.everrent_be.domain.product.model.Product;
import com.finalproject.everrent_be.global.common.ResponseDto;
import com.finalproject.everrent_be.global.util.Check;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Component
@RequiredArgsConstructor
public class ChatRoomService {

    private final Check check;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomJpaRepository chatRoomJpaRepository;
    private final InvitedMembersRepository invitedMembersRepository;
    private final MemberService memberService;
    /*
     * destination 에서 roomid 가져오기
     */
    public String getRoomId(String destination) {
        int lastIndex = destination.lastIndexOf('/');
        if (lastIndex != -1) {
            return destination.substring(lastIndex + 1);
        } else {
            return "";
        }
    }

    @Transactional
    public ResponseDto<?> createChatRoom(String productId) {
        Member member = memberService.getMemberfromContext();

        Product product = check.getCurrentProduct(productId);
        check.checkProduct(product);

        Optional<ChatRoom> chatRoom = chatRoomJpaRepository.findByMemberAndProduct(member, product);
        if (chatRoom.isPresent()) {
            Optional<InvitedMembers> invitedMembers = invitedMembersRepository.findByMemberIdAndRoomId(member.getId(), chatRoom.get().getRoomId());
            if (invitedMembers.isPresent()) {
                return ResponseDto.is_Success(chatRoom.get().getRoomId());
            }

        }
        ChatRoom createChatRoom = ChatRoom.create(product, member);
        chatRoomRepository.createChatRoom(createChatRoom);
        //뱃지5-채팅50회이상
        List<ChatRoom> mychat=chatRoomJpaRepository.findAllByMemberId(member.getId());
        if(mychat.size()==50){
            member.setBadges(5,"1");
        }
        return ResponseDto.is_Success(createChatRoom.getRoomId());
    }
}
