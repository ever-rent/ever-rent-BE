package com.finalproject.everrent_be.domain.chat.service;


import com.finalproject.everrent_be.domain.chat.dto.RoomInfoRequestDto;
import com.finalproject.everrent_be.domain.chat.dto.RoomInfoResponseDto;
import com.finalproject.everrent_be.domain.chat.dto.RoomInviteDto;
import com.finalproject.everrent_be.domain.chat.model.Chat;
import com.finalproject.everrent_be.domain.chat.model.RoomDetail;
import com.finalproject.everrent_be.domain.chat.model.RoomInfo;
import com.finalproject.everrent_be.domain.chat.repository.ChatRepository;
import com.finalproject.everrent_be.domain.chat.repository.RoomDetailRepository;
import com.finalproject.everrent_be.domain.chat.repository.RoomInfoRepository;
import com.finalproject.everrent_be.domain.member.model.Member;
import com.finalproject.everrent_be.domain.member.repository.MemberRepository;
import com.finalproject.everrent_be.domain.product.model.Product;
import com.finalproject.everrent_be.domain.product.repository.ProductRepository;
import com.finalproject.everrent_be.global.redis.repository.RedisRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoomService {
    private final RoomInfoRepository roomInfoRepository;
    private final MemberRepository memberRepository;
    private final RoomDetailRepository roomDetailsRepository;
    private final ChatRepository chatRepository;
    private final RedisRepository redisRepository;

    private final ProductRepository productRepository;

    @Transactional
    public RoomInfoResponseDto createRoom(String memberName, Long me, Long memberId, Long itemId, String title) {
        Member member = memberRepository.findById(me).orElseThrow();
        if(me.equals( memberId)) throw new IllegalArgumentException( "자신의 게시글 입니다.");

        RoomInfoRequestDto RequestDto = new RoomInfoRequestDto(memberName, member.getId(), memberId, itemId, title);
        return createRoom(member, RequestDto);
    }

    @Transactional
    public RoomInfoResponseDto createRoom(Member member, RoomInfoRequestDto requestDto) {
        Product product = productRepository.findById(requestDto.getProductId())
                .orElseThrow(()-> new IllegalArgumentException("해당하는 게시글이 없습니다."));
        RoomDetail room = roomDetailsRepository.findByMember_IdAndProduct_Id(member.getId(), requestDto.getProductId())/*맴버와 아이템 아이디 값이 없으면 빌드실행*/
                .orElseGet(() ->{
                    RoomInfo roomInfo = RoomInfo.builder()
                            .member(member)
                            .product(product)
                            .memberName(requestDto.getMemberName())
                            .title(requestDto.getTitle())
                            .roomDetail(new ArrayList<>())
                            .build();
                    RoomDetail roomDetail = RoomDetail.builder()
                            .product(product)
                            .member(member)
                            .roomInfo(roomInfo)
                            .build();

                    roomInfo.getRoomDetail().add(roomDetail);
                    roomInfoRepository.save(roomInfo);
                    redisRepository.subscribe(roomInfo.getId().toString());

                    return roomDetail;
                });
        return RoomInfoResponseDto.Info(room);
    }


    @Transactional
    public void updateLastReadChat(Long roomId, Long memberId, Long productId) {
        RoomDetail detail = roomDetailsRepository.findByRoomInfo_IdAndMember_IdAndProduct_Id(roomId, memberId, productId)
                .orElseThrow(() -> new IllegalArgumentException("채팅방에 속해있지 않은 회원입니다."));

        Chat chat = chatRepository.findFirstByRoomDetail_RoomInfo_IdOrderByCreatedAtDesc(roomId)
                .orElseThrow(() -> new IllegalArgumentException("채팅 내역이 존재하지 않습니다."));

        detail.updateChatId(chat.getId());
    }


    @Transactional(readOnly = true)
    public List<RoomInfoResponseDto> getRoomInfo(String memberId) {
        Member member = memberRepository.findById(Long.parseLong(memberId)).orElseThrow();
        return getRoomInfo(member);
    }

    @Transactional(readOnly = true)
    public List<RoomInfoResponseDto> getRoomInfo(Member member) {
        List<RoomDetail> allByOrderByModifiedAtDesc =  roomDetailsRepository.findAllByMemberOrderByModifiedAtDesc(member);
        return allByOrderByModifiedAtDesc.stream()
                .map(RoomInfoResponseDto::Info)
                .collect(Collectors.toList());

        /*차후 채팅창 연결시 필요 할 수도 있으니 주석처리 함.*/
//                List<RoomInfoResponseDto> dtos = new ArrayList<>();
//
//                for (RoomInfo roomInfo : allByOrderByModifiedAtDesc) {
//                        Long roomInfoId = roomInfo.getId();
//                        String nickname = member.getNickname();
//                        String createdAt = String.valueOf(roomInfo.getCreatedAt());
//
//                        RoomInfoResponseDto responseDto = new RoomInfoResponseDto(roomInfoId, nickname,createdAt);
//                        dtos.add(responseDto);
//                }
    }

    @Transactional
    public void deleteRoomInfo(Member member, Long roomInfoId) {
        RoomInfo roomInfo = roomInfoRepository.findById(roomInfoId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 채팅창입니다."));
        if (!member.getId().equals(roomInfo.getMember().getId()))
            throw new IllegalArgumentException("채팅방에 존재하지 않는 유저입니다.");
        roomInfoRepository.delete(roomInfo);
    }

    @Transactional
    public void inviteRoom(Long memberId, Long roomId, RoomInviteDto inviteDto) {
        Member member = memberRepository.findById(memberId).orElseThrow();
        inviteRoom(member, roomId, inviteDto);
    }

    public void inviteRoom(Member me, Long roomInfoId, RoomInviteDto inviteDto) {
        RoomInfo roomInfo = roomInfoRepository.findById(roomInfoId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 채팅창 입니다."));
        if (!me.getId().equals(roomInfo.getMember().getId()))
            throw new IllegalArgumentException("권한이 없습니다.");
        Member member = memberRepository.findById(inviteDto.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("초대 대상이 올바르지 않습니다."));
        log.info(member.toString());
        Product product = productRepository.findById(inviteDto.getProductId())
                .orElseThrow(()-> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));
        log.info(product.toString());
        RoomDetail roomDetail = roomDetailsRepository.findByRoomInfo_IdAndMember_IdAndProduct_Id(roomInfoId, inviteDto.getMemberId(),inviteDto.getProductId())
                .orElse(new RoomDetail(roomInfo, member, product));

        roomDetailsRepository.save(roomDetail);

    }
}
