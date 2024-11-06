package com.subin.point.dto.point;

import lombok.Getter;

@Getter
public class EarnCancelRequestDTO {
    // 회원 ID
    private Long memberId;
    // 적립 취소 Point
    private Long amount;
}
