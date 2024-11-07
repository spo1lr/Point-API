package com.subin.point.dto.member;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class CreateMemberRequestDTO {
    // 이름
    @NotNull(message = "이름을 입력해주세요.")
    @Size(min = 1, max = 30, message = "이름은 1자 이상 30자 이하이어야 합니다.")
    private String name;
}
