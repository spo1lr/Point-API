package com.subin.point.dto.reponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
@AllArgsConstructor
public enum Code {
    // 공통
    REQUEST_SUCCESS(HttpStatus.OK, "성공"),
    // 회원
    NOT_FOUND_MEMBER(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
    DUPLICATE_NAME(HttpStatus.BAD_REQUEST, "중복된 닉네임 입니다."),
    // 포인트
    MAX_EARN_POINT_OVER(HttpStatus.BAD_REQUEST, "1회 적립 가능한 포인트를 초과했습니다."),
    MAX_POINTS_EXCEEDED(HttpStatus.BAD_REQUEST, "보유 가능한 포인트를 초과했습니다."),
    EXPIRES_OUT_OF_RANGE(HttpStatus.BAD_REQUEST, "만료일은 1일 이상, 5년 미만이어야 합니다."),
    NOT_ENOUGH_POINT(HttpStatus.BAD_REQUEST, "사용 가능한 포인트가 부족합니다."),
    NOT_ENOUGH_CANCEL_POINT(HttpStatus.BAD_REQUEST, "취소 가능한 포인트가 부족합니다."),
    CANCEL_ONLY_UNUSED_POINTS(HttpStatus.BAD_REQUEST, "미사용된 적립 포인트 단위로 취소 가능합니다."),
    NOT_FOUND_USING_POINT(HttpStatus.NOT_FOUND, "사용된 포인트 내역이 존재하지 않습니다.");

    private final HttpStatus status;
    private final String message;
}
