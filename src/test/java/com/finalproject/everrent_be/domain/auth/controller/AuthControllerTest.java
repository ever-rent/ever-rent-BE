package com.finalproject.everrent_be.domain.auth.controller;

import com.finalproject.everrent_be.domain.auth.service.AuthService;
import com.finalproject.everrent_be.domain.member.dto.MemberRequestDto;
import com.finalproject.everrent_be.domain.member.dto.MemberResponseDto;
import com.finalproject.everrent_be.domain.member.model.Member;
import com.finalproject.everrent_be.global.common.ResponseDto;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)//JUniit5와 Mockito 연동
class AuthControllerTest {
    /*

    @InjectMocks //가짜 객체 주입
    private AuthController authController;

    @Mock //가짜 객체
    private AuthService authService;

    //http호출을 위해
    private MockMvc mockMvc;

    @BeforeEach
    public void init(){
        mockMvc= MockMvcBuilders.standaloneSetup(authController).build();

    }

    ///준비끝///

    @DisplayName("회원 가입 성공")
    @Test
    void signUpSuccess() throws Exception{
        //given
        MemberRequestDto memberRequestDto= new MemberRequestDto();
        MemberResponseDto memberResponseDto=new MemberResponseDto();

        doReturn(memberResponseDto).when(authService).signup(any(MemberRequestDto.class));
        //when : POST요청
        ResultActions resultActions=mockMvc.perform(
                MockMvcRequestBuilders.post("/signups")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(memberRequestDto)) //보내는 데이터는 객체가 아닌 문자열이여야 하므로 별도의 변환이 필요하므로 Gson을 사용해 변환
    );
        //then
        MvcResult mvcResult=  resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("id",memberResponseDto.getId()).exists())
                .andExpect(jsonPath("email",memberResponseDto.getEmail()).exists())
                .andExpect(jsonPath("memberName",memberResponseDto.getMemberName()).exists())
                .andExpect(jsonPath("mainAddress",memberResponseDto.getMainAddress()).exists())
                .andExpect(jsonPath("subAddress",memberResponseDto.getSubAddress()).exists()).andReturn();
    }

    private MemberRequestDto memberRequestDto(){
        return MemberRequestDto.builder()
                .email("test@test.test")
                .password("12341234")
                .memberName("testname")
                .mainAddress("서울시 중랑구")
                .subAddress("서울시 마포구")
                .build();
    }
    private MemberResponseDto memberResponseDto(){
        return MemberResponseDto.builder()
                .id(1L)
                .email("test@test.test")
                .memberName("testname")
                .mainAddress("서울시 중랑구")
                .subAddress("서울시 마포구")
                .policy(true)
                .build();
    }

     */



}