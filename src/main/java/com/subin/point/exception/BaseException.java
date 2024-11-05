package com.subin.point.exception;

import com.subin.point.dto.reponse.Code;
import lombok.Getter;

@Getter
public abstract class BaseException extends RuntimeException {
    private final Code code;

    protected BaseException(Code code) {
        super(code.getMessage());
        this.code = code;
    }
}
