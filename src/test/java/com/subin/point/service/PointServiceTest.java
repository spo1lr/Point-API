package com.subin.point.service;

import com.subin.point.entity.Member;
import com.subin.point.entity.Point;
import com.subin.point.entity.PointTransaction;
import com.subin.point.repository.MemberRepository;
import com.subin.point.repository.PointRepository;
import com.subin.point.repository.PointTransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
public class PointServiceTest {
    @Autowired
    private PointService pointService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private PointRepository pointRepository;
    @Autowired
    private PointTransactionRepository pointTransactionRepository;


    private Member member;

    @Value("${point.max-earn-point}")
    private Long maxEarnPoint;

    @Value("${point.max-hold-point}")
    private Long maxHoldPoint;

    @BeforeEach
    public void setUp() {
        member = new Member();
        member.setName("사용자1");
        member = memberRepository.save(member);
    }

    @Test
    public void 포인트적립_성공_테스트() {
        // Given
        Long amount = 5000L;
        boolean isManual = true;
        int expireDays = 30;

        // When
        Point point = pointService.earn(member.getId(), amount, isManual, expireDays);

        // Then
        assertNotNull(point);
        assertEquals(member.getId(), point.getMember().getId());
        assertEquals(amount, point.getAmount());
        assertEquals(isManual, point.isManual());
        assertEquals(0L, point.getUsedAmount());
        assertNotNull(point.getExpireAt());
    }

    @Test
    public void 적립가능한_최대_포인트_테스트() {
        // Given - 적립가능한 최대 포인트보다 큰값 설정
        Long amount = maxEarnPoint + 1;

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            pointService.earn(member.getId(), amount, true, 30);
        });
    }

    @Test
    public void 최대보유포인트_초과_테스트() {
        // Given - 현재 보유 포인트를 최대 보유 포인트보다 낮게 설정
        Long existingAmount = maxHoldPoint - 5000L;

        Point existingPoint = Point.createPoint(member, existingAmount, true, 365);
        pointRepository.save(existingPoint);

        Long amount = 10000L;

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            pointService.earn(member.getId(), amount, true, 30);
        });
    }

    @Test
    public void 유효하지않은_만료일수_포인트적립_테스트() {
        // Given
        Long amount = 5000L;

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            pointService.earn(member.getId(), amount, true, 0);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            pointService.earn(member.getId(), amount, true, 365 * 5);
        });
    }

    @Test
    public void 포인트사용_성공_테스트() {
        // Given
        Long earnPoint = 10000L;
        Long usePoint = 11000L;
        String orderId = "ORDER001";

        pointService.earn(member.getId(), earnPoint, true, 30);
        pointService.earn(member.getId(), earnPoint, true, 30);

        // When
        pointService.usePoint(member.getId(), usePoint, orderId);

        // Then
        List<PointTransaction> transactions = pointTransactionRepository.findByOrderId(orderId);
        assertEquals(2, transactions.size());
        assertEquals(earnPoint, transactions.get(0).getAmount());
        assertEquals(usePoint - earnPoint, transactions.get(1).getAmount());

        Point point1 = pointRepository.findById(transactions.get(0).getPoint().getId()).orElseThrow();
        Point point2 = pointRepository.findById(transactions.get(1).getPoint().getId()).orElseThrow();

        assertEquals(earnPoint, point1.getUsedAmount());
        assertEquals(usePoint - earnPoint, point2.getUsedAmount());
    }

    @Test
    public void 포인트사용_잔액부족_테스트() {
        // Given - 적립된 포인트보다 큰값 설정
        pointService.earn(member.getId(), maxEarnPoint, true, 30);

        Long useAmount = maxEarnPoint + 1;
        String orderId = "ORDER001";

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            pointService.usePoint(member.getId(), useAmount, orderId);
        });
    }
}
