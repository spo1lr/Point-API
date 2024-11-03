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
    @JoinColumn(name = "point_id")
    private Point point;

    private Long amount;
    private String orderId;

    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    // 포인트 차감 트랙잭션 생성
    public static PointTransaction createTransaction(Long amount, String orderId, TransactionType type, Point point) {
        PointTransaction transaction = new PointTransaction();
        transaction.setAmount(amount);
        transaction.setOrderId(orderId);
        transaction.setType(type);
        transaction.setPoint(point);
        transaction.setCreatedAt(LocalDateTime.now());
        return transaction;
    }
}
