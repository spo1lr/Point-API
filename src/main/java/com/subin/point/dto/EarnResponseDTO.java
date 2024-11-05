package com.subin.point.dto;

import com.subin.point.entity.Point;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
public class EarnResponseDTO {
    // 포인트 ID
    private Long pointId;
    // 회원 ID
    private Long memberId;
    // 적립 Point 금액
    private Long amount;
    // 관리자 수기 지급 여부
    private boolean isManual;
    // 포인트 만료일
    private LocalDateTime expireAt;

    public EarnResponseDTO(Point point) {
        this.pointId = point.getId();
        this.memberId = point.getMember().getId();
        this.amount = point.getAmount();
        this.isManual = point.isManual();
        this.expireAt = point.getExpireAt();
    }
}
