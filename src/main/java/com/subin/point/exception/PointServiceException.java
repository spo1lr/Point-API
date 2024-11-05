package com.subin.point.exception;

import com.subin.point.dto.reponse.Code;

public class PointServiceException extends BaseException {

    public PointServiceException(Code code) {
        super(code);
    }
}
