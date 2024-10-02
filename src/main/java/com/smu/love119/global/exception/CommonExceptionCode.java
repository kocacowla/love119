package com.smu.love119.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/*
 *
 * 발생할 수 있는 에러 코드(공통부분)
 */
@Getter
@RequiredArgsConstructor
public enum CommonExceptionCode implements ExceptionCode {

    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "Invalid parameter included"),
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "Resource not exists"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error"),
    USER_NOT_MATCH(HttpStatus.FORBIDDEN, "User does not match")
    ;

    private final HttpStatus httpStatus;
    private final String message;
}