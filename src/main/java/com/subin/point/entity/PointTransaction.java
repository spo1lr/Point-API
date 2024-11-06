package com.subin.point.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
public class PointTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "point_id", columnDefinition = "BIGINT COMMENT '포인트 ID'")
    private Point point;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", columnDefinition = "BIGINT COMMENT '회원 ID'")
    private Member member;

    @Column(nullable = false, columnDefinition = "BIGINT COMMENT '거래 포인트 금액'")
    private Long amount;

    @Column(columnDefinition = "VARCHAR(255) COMMENT '주문 ID'")
    private String orderId;

    @Column(nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '거래 생성일시'")
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "VARCHAR(20) COMMENT '거래 유형'")
    private TransactionType type;

    // 포인트 차감 트랙잭션 생성
    public static PointTransaction createTransaction(Long amount, String orderId, TransactionType type, Member member, Point point) {
        PointTransaction transaction = new PointTransaction();
        transaction.setAmount(amount);
        transaction.setOrderId(orderId);
        transaction.setType(type);
        transaction.setMember(member);
        transaction.setPoint(point);
        transaction.setCreatedAt(LocalDateTime.now());
        return transaction;
    }
}
