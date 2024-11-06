package com.subin.point.dto.point;

import lombok.Getter;

@Getter
public class UseCancelRequestDTO {
    // 취소할 회원 ID
    private Long memberId;
    // 주문 ID
    private String orderId;
    // 취소할 포인트 금액
    private Long amount;
}
