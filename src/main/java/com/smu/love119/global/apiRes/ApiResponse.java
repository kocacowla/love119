package com.smu.love119.global.apiRes;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Builder
@Getter
public class ApiResponse<T> {

    private final HttpStatus status;
    private final String message;
    private final T data;

    public static <T> ApiResponse<T> successRes(HttpStatus status, T data) {
        return new ApiResponse<>(status, "Success", data); // 기본 메시지 사용
    }

    public static <T> ApiResponse<T> failureRes(HttpStatus status, String message, T data) {
        return new ApiResponse<>(status, message, data);
    }

    public static <T> ApiResponse<T> errorRes(HttpStatus status, String message, T data) {
        return new ApiResponse<>(status, message, data);
    }
}
