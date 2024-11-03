package com.subin.point.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EarnRequestDTO {
    // 회원 ID
    private Long memberId;
    // 적립 Point 금액
    private Long amount;
    // 관리자 수기 지급 여부
    private boolean isManual;
    // 포인트 만료일
    private Integer expireDays;
}
