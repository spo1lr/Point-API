package com.subin.point.dto.reponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
@AllArgsConstructor
public enum Code {
    // 공통
    REQUEST_SUCCESS(HttpStatus.OK, "성공");

    private final HttpStatus status;
    private final String message;
}
