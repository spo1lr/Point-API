package com.subin.point.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class Point {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "member_id", columnDefinition = "BIGINT COMMENT '회원 ID'")
    private Member member;

    @Column(nullable = false, columnDefinition = "BIGINT COMMENT '포인트 금액'")
    private Long amount;

    @Column(nullable = false, columnDefinition = "BIGINT DEFAULT 0 COMMENT '사용된 포인트 금액'")
    private Long usedAmount = 0L;

    @Column(nullable = false, columnDefinition = "BOOLEAN COMMENT '관리자 수기 지급 여부'")
    private boolean isManual;

    @Column(columnDefinition = "TIMESTAMP COMMENT '만료일시'")
    private LocalDateTime expireAt;

    @Column(columnDefinition = "TIMESTAMP COMMENT '취소일시'")
    private LocalDateTime canceledAt;

    @OneToMany(mappedBy = "point", cascade = CascadeType.ALL)
    private List<PointTransaction> transactions = new ArrayList<>();

    @Version
    private Long version;

    // 사용가능한 포인트 계산
    public Long getAvailableAmount() {
        return amount - usedAmount;
    }

    private void addToMember(Member member) {
        this.member = member;
        member.getPoints().add(this);
    }

    // 포인트 적립
    public static Point createPoint(Member member, Long amount, boolean isManual, int daysToExpire) {
        Point point = new Point();
        point.member = member;
        point.amount = amount;
        point.isManual = isManual;
        point.expireAt = LocalDateTime.now().plusDays(daysToExpire);
        point.addToMember(member);
        return point;
    }
}
