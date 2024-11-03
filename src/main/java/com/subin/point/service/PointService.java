package com.subin.point.service;

import com.subin.point.entity.Member;
import com.subin.point.entity.Point;
import com.subin.point.entity.PointTransaction;
import com.subin.point.entity.TransactionType;
import com.subin.point.repository.MemberRepository;
import com.subin.point.repository.PointRepository;
import com.subin.point.repository.PointTransactionRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class PointService {

    private final MemberRepository memberRepository;
    private final PointRepository pointRepository;
    private final PointTransactionRepository pointTransactionRepository;

    @Value("${point.default-expire-days}")
    private int defaultExpireDays;

    @Value("${point.max-earn-point}")
    private Long maxEarnPoint;

    @Value("${point.max-hold-point}")
    private Long maxHoldPoint;

    // 포인트 적립
    @Transactional
    public Point earn(Long memberId, Long amount, boolean isManual, Integer expireDays) {
        // 최대 포인트 적립 검증
        if (amount < 1 || amount > maxEarnPoint) {
            throw new IllegalArgumentException("1회 적립 가능한 포인트를 초과했습니다.");
        }

        // 사용자 조회
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));
        // 잔여 포인트 조회
        Long totalPoints = pointRepository.availablePointsByMember(member).stream().mapToLong(Point::getAvailableAmount).sum();

        // 최대보유가능 포인트 검증
        if (totalPoints + amount > maxHoldPoint) {
            throw new IllegalArgumentException("보유 가능 무료 포인트를 초과했습니다.");
        }

        // 적립 포인트 만료일자 설정 (기본 365일)
        int daysToExpire = expireDays != null ? expireDays : defaultExpireDays;
        if (daysToExpire < 1 || daysToExpire >= 365 * 5) {
            throw new IllegalArgumentException("만료일은 1일 이상, 5년 미만이어야 합니다.");
        }

        // 포인트 적립 영속화
        Point point = Point.createPoint(member, amount, isManual, daysToExpire);
        return pointRepository.save(point);
    }

    // 포인트 사용
    @Transactional
    public void usePoint(Long memberId, Long amount, String orderId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));

        // 포인트 사용 순서 설정
        // 1. 관리자 수기지급 포인트, 2. 만료일이 짧게 남은 포인트
        List<Point> points = pointRepository.availablePointsByMember(member).stream()
                .filter(p -> p.getAvailableAmount() > 0)
                .sorted(Comparator.comparing(Point::isManual).reversed()
                        .thenComparing(Point::getExpireAt))
                .toList();

        if (points.stream().mapToLong(Point::getAvailableAmount).sum() < amount) {
            throw new IllegalArgumentException("사용 가능한 포인트가 부족합니다.");
        }

        Long remainingAmount = amount;
        for (Point point : points) {
            Long available = point.getAvailableAmount();
            if (available <= 0) continue;

            // 포인트 차감
            Long useAmount = Math.min(available, remainingAmount);
            point.setUsedAmount(point.getUsedAmount() + useAmount);
            pointRepository.save(point);

            // 트랜잭션 차감 트랜잭션 생성
            PointTransaction transaction = PointTransaction.createTransaction(useAmount, orderId, TransactionType.USE, point);
            pointTransactionRepository.save(transaction);

            remainingAmount -= useAmount;
            if (remainingAmount == 0) break;
        }
    }
}
