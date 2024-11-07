package com.subin.point.exception.handler;

import com.subin.point.exception.BaseException;
import com.subin.point.exception.handler.dto.ExceptionResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Optional;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ExceptionResponseDTO> baseCustomException(BaseException e) {
        return ResponseEntity.status(e.getCode().getStatus()).body(new ExceptionResponseDTO(e.getCode().name(), e.getMessage()));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ExceptionResponseDTO methodArgumentNotValidException(MethodArgumentNotValidException e) {
        String message = Optional.ofNullable(e.getBindingResult().getFieldError())
                .map(fieldError -> String.format("%s", fieldError.getDefaultMessage()))
                .orElse("Invalid request parameter");
        return new ExceptionResponseDTO("INVALID_PARAMETER", message);
    }
}
