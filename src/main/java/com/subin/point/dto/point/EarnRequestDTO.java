package com.subin.point.dto.point;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EarnRequestDTO {
    // 회원 ID
    @NotNull(message = "회원 id를 입력해주세요.")
    private Long memberId;

    // 적립 Point 금액
    @NotNull(message = "포인트를 입력해주세요.")
    @Min(value = 1, message = "1 이상의 포인트를 입력해주세요.")
    private Long amount;

    // 관리자 수기 지급 여부
    private boolean isManual;

    // 포인트 만료일
    @NotNull(message = "포인트 만료일를 입력해주세요.")
    @Min(value = 1, message = "포인트 만료일은 1 이상이어야 합니다.")
    private Integer expireDays;
}
