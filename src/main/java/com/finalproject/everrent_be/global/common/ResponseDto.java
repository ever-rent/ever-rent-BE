package com.finalproject.everrent_be.global.common;

import com.finalproject.everrent_be.global.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResponseDto<T> {

    public boolean is_success;
    public T data;
    public ErrorCode errorCode;
    public T best;

    public static <T> ResponseDto<T> is_Success(T data)
    {
        return new ResponseDto<T>(true,data,null);
    }
    public static <T> ResponseDto<T> is_Success(T best,T data){
        return new ResponseDto<T>(true,best,data,null);
    }
    public ResponseDto(boolean is_success, T data, ErrorCode errorCode){
        this.is_success=is_success;
        this.data=data;
        this.errorCode=errorCode;

    }
    public ResponseDto(boolean is_success,T best, T data, ErrorCode errorCode){
        this.is_success=is_success;
        this.best=best;
        this.data=data;
        this.errorCode=errorCode;

    }
    public static <T> ResponseDto<T> is_Fail(ErrorCode errorCode){return new ResponseDto<T>(false,null,errorCode);}
}