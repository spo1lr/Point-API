package com.subin.point.dto.point;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class EarnCancelRequestDTO {
    // 회원 ID
    @NotNull(message = "회원 id를 입력해주세요.")
    private Long memberId;

    // 적립 취소 Point
    @NotNull(message = "포인트를 입력해주세요.")
    @Min(value = 1, message = "1 이상의 포인트를 입력해주세요.")
    private Long amount;
}
