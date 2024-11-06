package com.subin.point.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 64)
    private String name;

    @Column(nullable = false)
    private Long maxEarnPoint;

    @Column(nullable = false)
    private Long maxHoldPoint;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Point> points = new ArrayList<>();

    // 신규 회원 생성
    public static Member createMember(String name) {
        Member member = new Member();
        member.setName(name);
        member.setMaxEarnPoint(100000L);  // 1회 최대 적립 포인트 기본값: 10만
        member.setMaxHoldPoint(150000L);  // 최대 보유 포인트 기본값: 15만
        return member;
    }

    // 회원 적립, 보유 포인트 설정 변경
    public void updateMemberPointSettings(Long maxEarnPoint, Long maxHoldPoint) {
        this.setMaxEarnPoint(maxEarnPoint);
        this.setMaxHoldPoint(maxHoldPoint);
    }
}
