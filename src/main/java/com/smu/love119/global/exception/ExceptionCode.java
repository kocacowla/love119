package com.smu.love119.global.exception;

import org.springframework.http.HttpStatus;



public interface ExceptionCode {

    //클라이언트에게 보내줄 에러 코드 정의
    //에러 이름, HTTP 상태 및 메시지
    String name();
    HttpStatus getHttpStatus();
    String getMessage();

}