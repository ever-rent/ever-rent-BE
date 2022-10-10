package com.finalproject.everrent_be.domain.chat.controller;

import com.finalproject.everrent_be.domain.chat.dto.RoomInfoRequestDto;
import com.finalproject.everrent_be.domain.chat.dto.RoomInfoResponseDto;
import com.finalproject.everrent_be.domain.chat.dto.RoomInviteDto;
import com.finalproject.everrent_be.domain.chat.model.RoomInfo;
import com.finalproject.everrent_be.domain.chat.repository.RoomInfoRepository;
import com.finalproject.everrent_be.domain.chat.service.RoomService;
import com.finalproject.everrent_be.domain.member.model.Member;
import com.finalproject.everrent_be.domain.member.service.MemberService;
import com.finalproject.everrent_be.global.util.UserDetailsImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@Api(tags = {"채팅방 API 정보를 제공하는 Controller"})
@RequiredArgsConstructor
@RestController
public class RoomController {

    private final RoomService roomService;
    private final RoomInfoRepository roomInfoRepository;
    private final MemberService memberService;

    @ApiOperation(value = "채팅방 생성 메소드")
    @PostMapping(value = "/roomInfo")
    public ResponseEntity<?> createRoom(@RequestBody RoomInfoRequestDto requestDto) {
        Member member = memberService.getMemberfromContext();
        return ResponseEntity.ok().body(Map.of("msg", "성공","roomInfo",roomService.createRoom(member,requestDto)));
    }

    @ApiOperation(value = "채팅방 정보 조회 메소드")
    @GetMapping(value = "/roomInfo")
    public ResponseEntity<?> getRoomInfo() {
        Member member = memberService.getMemberfromContext();
        List<RoomInfoResponseDto> ResponseDtos = roomService.getRoomInfo(member);
        return ResponseEntity.ok().body(ResponseDtos);
    }

    @ApiOperation(value = "채팅방 나가기")
    @DeleteMapping(value = "/rooms/{productId}")
    public ResponseEntity<?> deleteRoomInfo(@PathVariable String productId) {
        Member member = memberService.getMemberfromContext();
        RoomInfo roomInfo = roomInfoRepository.findByMember_IdAndProduct_Id(member.getId(), Long.valueOf(productId))
                .orElseThrow(()-> new IllegalArgumentException("존재하지 않는 채팅방 입니다."));
        roomService.deleteRoomInfo(member, roomInfo.getId());
        return ResponseEntity.ok().body(Map.of("success", true, "msg", "삭제 성공"));

    }

    @ApiOperation(value = "채팅방 입장 메소드")
    @PostMapping(value = "/rooms/{roomInfoId}")
    public ResponseEntity<?> inviteRoom(@PathVariable Long roomInfoId, @RequestBody RoomInviteDto inviteDto) {
        Member member = memberService.getMemberfromContext();
        roomService.inviteRoom(member, roomInfoId, inviteDto);
        return ResponseEntity.ok().body(Map.of("msg", "초대 성공"));
    }


}