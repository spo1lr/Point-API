package com.subin.point.dto;

import lombok.Getter;

@Getter
public class UseRequestDTO {
    // 회원 ID
    private Long memberId;
    // 사용 Point 금액
    private Long amount;
    // 주문번호
    private String orderId;
}
