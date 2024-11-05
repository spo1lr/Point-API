package com.subin.point.dto.reponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.ResponseEntity;

@Data
@AllArgsConstructor
public class Response<T> {

    private String code;
    private String message;
    private T data;

    public static <T> ResponseEntity<Response<T>> of(Code code, T data) {
        return ResponseEntity.status(code.getStatus()).body(new Response<>(code.name(), code.getMessage(), data));
    }

    public static <T> ResponseEntity<Response<T>> of(Code code) {
        return of(code, null);
    }
}
