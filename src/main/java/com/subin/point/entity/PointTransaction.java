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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private Long amount;
    private String orderId;

    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
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
