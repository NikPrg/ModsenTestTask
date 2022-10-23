package com.example.eventsrestapi.api;

import com.example.eventsrestapi.exception.EventException;
import com.example.eventsrestapi.exception.EventNotExistException;
import lombok.Builder;
import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EventException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorInfoResponse handleRuntimeException(EventException exception) {
        return ErrorInfoResponse.createErrorInfoResponse(exception);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorInfoResponse onConstraintValidationException(ConstraintViolationException exception) {
        return ErrorInfoResponse.createErrorInfoResponse(exception);
    }


    @Value
    @Builder
    private static class ErrorInfoResponse {
        List<ErrorInfo> errorInfos;

        static ErrorInfoResponse createErrorInfoResponse(EventException exception) {
            return ErrorInfoResponse.builder()
                    .errorInfos(Collections.singletonList(
                            ErrorInfo.builder()
                                    .message(exception.getMessage())
                                    .timestamp(LocalDateTime.now().toString())
                                    .build()))
                    .build();
        }

        static ErrorInfoResponse createErrorInfoResponse(ConstraintViolationException exception) {
            return ErrorInfoResponse.builder()
                    .errorInfos(exception.getConstraintViolations().stream()
                            .map(violation -> ErrorInfo.builder()
                                    .message(violation.getMessage())
                                    .field(violation.getPropertyPath().toString())
                                    .timestamp(LocalDateTime.now().toString())
                                    .build())
                            .collect(Collectors.toList())
                    )
                    .build();
        }
    }

    @Value
    @Builder
    private static class ErrorInfo {

        String message;
        String field;
        String timestamp;

    }
}


