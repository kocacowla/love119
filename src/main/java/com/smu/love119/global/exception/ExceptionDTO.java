package com.smu.love119.global.exception;


import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class ExceptionDTO {

    private final String errorCode;
    private final String errorMessage;

}