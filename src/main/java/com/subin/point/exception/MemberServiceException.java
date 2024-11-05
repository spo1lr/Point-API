package com.subin.point.exception;

import com.subin.point.dto.reponse.Code;

public class MemberServiceException extends BaseException {

    public MemberServiceException(Code code) {
        super(code);
    }
}
