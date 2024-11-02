package com.subin.point.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Point {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    private Long amount;
    private Long usedAmount = 0L;
    private LocalDateTime expireAt;
    private boolean isManual;
    private LocalDateTime canceledAt;


    // 사용가능한 포인트 계산
    public Long getAvailableAmount() {
        return amount - usedAmount;
    }
}
