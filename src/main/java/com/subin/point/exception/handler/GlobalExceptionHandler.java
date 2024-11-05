package com.subin.point.exception.handler;

import com.subin.point.exception.BaseException;
import com.subin.point.exception.handler.dto.ExceptionResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ExceptionResponseDTO> baseCustomException(BaseException e) {
        return ResponseEntity.status(e.getCode().getStatus()).body(new ExceptionResponseDTO(e.getCode().name(), e.getMessage()));
    }
}
