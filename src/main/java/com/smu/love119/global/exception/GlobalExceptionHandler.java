package com.smu.love119.global.exception;

import com.smu.love119.global.apiRes.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    //예외처리 Custom하는 부분
    @ExceptionHandler(RestApiException.class)
    public ResponseEntity<ApiResponse<Object>> handleCustomException(RestApiException e) {
        ExceptionCode exceptionCode = e.getExceptionCode();
        ApiResponse<Object> response = ApiResponse.errorRes(exceptionCode.getHttpStatus(), exceptionCode.getMessage(), null);
        return new ResponseEntity<>(response, exceptionCode.getHttpStatus());

    }

    //메소드에 전달된 인자가 유효하지 않을 때 발생하는 예외를 처리
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Object>> handleIllegalArgument(IllegalArgumentException e) {
        log.warn("handleIllegalArgument", e);
        ExceptionCode exceptionCode = CommonExceptionCode.INVALID_PARAMETER;
        ApiResponse<Object> response = ApiResponse.errorRes(exceptionCode.getHttpStatus(), exceptionCode.getMessage(), null);
        return new ResponseEntity<>(response, exceptionCode.getHttpStatus());
    }

    //@Valid 어노테이션을 사용한 유효성 검사에 실패했을 때 발생하는 예외 처리
    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException e,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {
        log.warn("handleIllegalArgument", e);
        ExceptionCode exceptionCode = CommonExceptionCode.INVALID_PARAMETER;
        ValidExceptionResponse exceptionResponse = makeErrorResponse(e, exceptionCode);
        ApiResponse<Object> response = ApiResponse.errorRes(exceptionCode.getHttpStatus(), exceptionCode.getMessage(), exceptionResponse);
        return new ResponseEntity<>(response, exceptionCode.getHttpStatus());
    }

    //
//    @Override
//    protected ResponseEntity<Object> handleHttpMessageNotReadable(
//            HttpMessageNotReadableException ex,
//            HttpHeaders headers,
//            HttpStatusCode status,
//            WebRequest request) {
//        ErrorCode errorCode = CommonErrorCode.RESOURCE_NOT_FOUND;
//        return handleExceptionInternal(errorCode);
//    }





    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleAllException(Exception ex) {
        log.warn("handleAllException", ex);
        ExceptionCode exceptionCode = CommonExceptionCode.INTERNAL_SERVER_ERROR;
        return handleExceptionInternal(exceptionCode);
    }


    private ResponseEntity<Object> handleExceptionInternal(ExceptionCode exceptionCode) {
        return ResponseEntity.status(exceptionCode.getHttpStatus())
                .body(makeErrorResponse(exceptionCode));
    }

    private ExceptionResponse makeErrorResponse(ExceptionCode exceptionCode) {
        return ExceptionResponse.builder()
                .status(exceptionCode.getHttpStatus())
                .message(exceptionCode.getMessage())
                .build();
    }

    private ResponseEntity<Object> handleExceptionInternal(ExceptionCode exceptionCode, String message) {
        return ResponseEntity.status(exceptionCode.getHttpStatus())
                .body(makeErrorResponse(exceptionCode, message));
    }

    private ExceptionResponse makeErrorResponse(ExceptionCode exceptionCode, String message) {
        return ExceptionResponse.builder()
                .status(exceptionCode.getHttpStatus())
                .message(message)
                .build();
    }

    private ResponseEntity<Object> handleExceptionInternal(BindException e, ExceptionCode exceptionCode) {
        return ResponseEntity.status(exceptionCode.getHttpStatus())
                .body(makeErrorResponse(e, exceptionCode));
    }


    //Valid 유효성 검사 에러 리스트 받아서 받환
    private ValidExceptionResponse makeErrorResponse(BindException e, ExceptionCode exceptionCode) {
        List<ValidExceptionResponse.ValidationError> validationErrorList = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(ValidExceptionResponse.ValidationError::of)
                .collect(Collectors.toList());

        return ValidExceptionResponse.builder()
                .errors(validationErrorList)
                .build();
    }

}
