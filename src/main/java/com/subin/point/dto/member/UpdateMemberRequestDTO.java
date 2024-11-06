package com.subin.point.dto.member;


import lombok.Getter;

@Getter
public class UpdateMemberRequestDTO {
    // 1회 최대 적립 포인트
    private Long maxEarnPoint;
    // 최대 보유 포인트
    private Long maxHoldPoint;
}
