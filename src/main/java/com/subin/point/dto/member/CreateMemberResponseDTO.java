package com.subin.point.dto.member;

import com.subin.point.entity.Member;
import lombok.Getter;

@Getter
public class CreateMemberResponseDTO {
    // 회원 ID
    private Long memberId;
    // 이름
    private String name;
    // 1회 최대 적립 포인트
    private Long maxEarnPoint;
    // 최대 보유 포인트
    private Long maxHoldPoint;

    public CreateMemberResponseDTO(Member member) {
        memberId = member.getId();
        name = member.getName();
        maxEarnPoint = member.getMaxEarnPoint();
        maxHoldPoint = member.getMaxHoldPoint();
    }
}
