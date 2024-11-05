package com.subin.point.exception.handler.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class ExceptionResponseDTO {
    private String code;
    private String message;
}