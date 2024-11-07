package com.subin.point.entity;

public enum TransactionType {
    // 포인트 적립
    EARN,
    // 포인트 적립 취소
    CANCEL,
    // 포인트 사용
    USE,
    // 포인트 사용 취소
    USECANCEL,
    // 만료 포인트 재적립
    REISSUE
}
