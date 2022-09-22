package com.finalproject.everrent_be.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    //로그인(토큰) 관련 오류
    NULL_TOKEN("NULL_TOKEN", "로그인이 필요합니다."),
    INVALID_TOKEN("INVALID_TOKEN", "Token이 유효하지 않습니다."),
    EXPIRED_TOKEN("EXPIRED_TOKEN", "만료된 토큰입니다."),
    MEMBER_NOT_FOUND("MEMBER_NOT_FOUND", "사용자를 찾을 수 없습니다."),
    MEMBER_NOT_ALLOWED("MEMBER_NOT_ALLOWED","본인이 작성한 글이 아닙니다."),

    //회원가입 관련 오류
    DUPLICATE_EMAIL("DUPLICATE_EMAIL", "중복된 이메일 입니다."),
    DUPLICATE_NICKNAME("DUPLICATE_NICKNAME", "중복된 닉네임 입니다."),
    //즐겨찾기 관련 오류
    DUPLICATE_FAVORITE("DUPLICATE_FAVORITE", "이미 즐겨찾기에 등록되었습니다."),

    INVALID_TIMESETTING("INVALID_TIMESETTING","기간 설정이 옳바르지 않습니다."),
    INVALID_CONDITION("INVALID_CONDITION","닉네임 또는 패스워드의 조건을 확인하세요."),

    FAVORITE_NOT_FOUND("FAVORITE_NOT_FOUND", "등록되지 않은 즐겨찾기입니다."),

    INVALID_START_DATE("INVALID_START_DATE","렌트 시작일을 확인해 주세요."),
    INVALID_END_DATE("INVALID_END_DATE","렌트 반납일을 확인해 주세요."),
    INVALID_CREATE("INVALID_CREATE", "본인이 작성한 상품은 구매할 수 없습니다."),

    //댓글 관련 오류
    COMMENT_NOT_FOUND("COMMENT_NOT_FOUND", "댓글을 찾을 수 없습니다."),
    INVALID_MEMBER("INVALID_MEMBER", "사용자가 일치하지 않습니다."),

    //이미지 업로드 관련 오류
    FAIL_TO_UPLOAD("FAIL_TO_UPLOAD", "이미지 업로드에 실패했습니다.");



    private final String code;
    private final String message;
}
