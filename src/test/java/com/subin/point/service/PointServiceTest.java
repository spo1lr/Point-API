package com.subin.point.service;

import com.subin.point.dto.reponse.Code;
import com.subin.point.entity.Member;
import com.subin.point.entity.Point;
import com.subin.point.entity.PointTransaction;
import com.subin.point.entity.TransactionType;
import com.subin.point.exception.PointServiceException;
import com.subin.point.repository.MemberRepository;
import com.subin.point.repository.PointRepository;
import com.subin.point.repository.PointTransactionRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
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

    @BeforeEach
    public void setUp() {
        member = memberRepository.save(Member.createMember("테스트"));
        memberRepository.flush();
    }

    @AfterEach
    public void tearDown() {
        pointTransactionRepository.deleteAllInBatch();
        pointRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
    }

    @Test
    public void 포인트적립_성공_테스트() {
        // Given - 포인트 적립: 5000 포인트, 관리자 지급, 만료일 30일
        Long amount = 5000L;
        boolean isManual = true;
        int expireDays = 30;

        // When
        Point point = pointService.earn(member.getId(), amount, isManual, expireDays);
        pointRepository.flush();

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
        Long amount = member.getMaxEarnPoint() + 1L;

        // When & Then
        assertThrows(PointServiceException.class, () -> {
            pointService.earn(member.getId(), amount, true, 30);
        });
    }

    @Test
    public void 최대보유포인트_초과_테스트() {
        // Given - 현재 보유 포인트를 최대 보유 포인트보다 낮게 설정
        Long maxHoldPoint = member.getMaxHoldPoint();
        Point existingPoint = Point.createPoint(member, maxHoldPoint, true, 365);
        pointRepository.save(existingPoint);
        pointRepository.flush();

        Long amount = maxHoldPoint + 1L;

        // When & Then
        PointServiceException exception =  assertThrows(PointServiceException.class, () -> {
            pointService.earn(member.getId(), amount, true, 30);
            pointRepository.flush();
        });
        assertEquals(Code.MAX_EARN_POINT_OVER, exception.getCode());

    }

    @Test
    public void 유효하지않은_만료일수_포인트적립_테스트() {
        // Given - 포인트 적립 : 5000 포인트
        Long amount = 5000L;

        // When & Then
        PointServiceException exception1 = assertThrows(PointServiceException.class, () -> {
            pointService.earn(member.getId(), amount, true, 0);
            pointRepository.flush();
        });
        assertEquals(Code.EXPIRES_OUT_OF_RANGE, exception1.getCode());

        PointServiceException exception2 = assertThrows(PointServiceException.class, () -> {
            pointService.earn(member.getId(), amount, true, 365 * 5);
            pointRepository.flush();
        });
        assertEquals(Code.EXPIRES_OUT_OF_RANGE, exception2.getCode());
    }

    @Test
    public void 포인트사용_성공_테스트() {
        // Given
        Long earnPoint = 10000L;
        Long usePoint = 11000L;
        String orderId = "ORDER001";

        pointService.earn(member.getId(), earnPoint, true, 30);
        pointRepository.flush();
        pointService.earn(member.getId(), earnPoint, true, 30);
        pointRepository.flush();

        // When
        pointService.usePoint(member.getId(), usePoint, orderId);
        pointRepository.flush();

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
        Long maxEarnPoint = member.getMaxEarnPoint();
        pointService.earn(member.getId(), maxEarnPoint, true, 30);
        pointRepository.flush();

        Long useAmount = maxEarnPoint + 1;
        String orderId = "ORDER001";

        // When & Then
        PointServiceException exception = assertThrows(PointServiceException.class, () -> {
            pointService.usePoint(member.getId(), useAmount, orderId);
            pointRepository.flush();
        });
        assertEquals(Code.NOT_ENOUGH_POINT, exception.getCode());
    }

    @Test
    public void 적립취소금액이_취소가능_포인트보다_큰_경우_테스트() {
        // Given - 포인트 적립 후 적립액 보다 큰 금액 적립 취소
        Long amount = 1000L;
        pointService.earn(member.getId(), amount, true, 30);
        pointRepository.flush();

        // 취소 금액 (적립액 + 1)
        Long cancelAmount = amount + 1L;

        // When & Then
        PointServiceException exception = assertThrows(PointServiceException.class, () -> {
            pointService.cancelEarnedPoint(member.getId(), cancelAmount);
        });
        assertEquals(Code.NOT_ENOUGH_CANCEL_POINT, exception.getCode());
    }

    @Test
    public void 적립취소금액이_취소가능_포인트와_같은_경우_테스트() {
        // Given - 적립된 금액 만큼 취소
        Long amount1 = 500L;
        Point earn1 = pointService.earn(member.getId(), amount1, true, 30);
        Long amount2 = 700L;
        Point earn2 = pointService.earn(member.getId(), amount2, true, 30);
        pointRepository.flush();

        // 취소 금액
        Long cancelAmount = amount1 + amount2;

        // When
        pointService.cancelEarnedPoint(member.getId(), cancelAmount);
        pointRepository.flush();

        // Then
        // 포인트 취소 여부 검증
        Point canceledAmount1 = pointRepository.findById(earn1.getId()).orElseThrow();
        Point canceledAmount2 = pointRepository.findById(earn2.getId()).orElseThrow();

        assertNotNull(canceledAmount1.getCanceledAt());
        assertNotNull(canceledAmount2.getCanceledAt());

        // 트랜잭션 생성여부 검증
        List<PointTransaction> transactions = pointTransactionRepository.findAllByType(TransactionType.CANCEL);
        assertEquals(2, transactions.size());
    }

    @Test
    public void 적립된_포인트_중_부분사용된_포인트는_적립취소_제외하는_테스트() {
        // Given
        // 첫 번째 포인트 적립: 500원 (사용된 금액: 200원)
        Long amount1 = 500L;
        Point earn1 = pointService.earn(member.getId(), amount1, true, 30);
        // 두 번째 포인트 적립: 700원 (미사용)
        Long amount2 = 700L;
        Point earn2 = pointService.earn(member.getId(), amount2, true, 30);
        pointRepository.flush();

        // 첫번째 적립 포인트 사용
        Long useAmount1 = 200L;
        pointService.usePoint(member.getId(), useAmount1, "ORDER001");
        pointRepository.flush();

        Long amountToCancel = 700L; // 사용되지 않은 포인트만 취소

        // When
        pointService.cancelEarnedPoint(member.getId(), amountToCancel);
        pointRepository.flush();

        // Then
        // 첫 번째 포인트는 취소되지 않아야 함
        Point updatedPoint1 = pointRepository.findById(earn1.getId()).orElseThrow();
        assertNull(updatedPoint1.getCanceledAt());

        // 두 번째 포인트는 취소되어야 함
        Point updatedPoint2 = pointRepository.findById(earn2.getId()).orElseThrow();
        assertNotNull(updatedPoint2.getCanceledAt());

        // 트랜잭션 생성여부 검증
        List<PointTransaction> transactions = pointTransactionRepository.findAllByType(TransactionType.CANCEL);
        assertEquals(1, transactions.size());
    }

    @Test
    public void 포인트_사용후_취소_테스트() {
        // Given - 기존 포인트 적립 후 사용
        Long amount = 5000L;
        Point point = pointService.earn(member.getId(), amount, true, 30);
        pointRepository.flush();
        // 포인트 사용
        Long useAmount1 = 2000L;
        pointService.usePoint(member.getId(), useAmount1, "ORDER001");
        pointRepository.flush();

        // When
        pointService.cancelPointUse(member.getId(), "ORDER001", 2000L);
        pointRepository.flush();

        // Then
        assertEquals(0L, point.getUsedAmount());
        assertEquals(amount, point.getAvailableAmount());
    }

    @Test
    public void 만료된_포인트_재적립_테스트() {
        // Given - 만료된 포인트로 적립 및 사용한 트랜잭션 생성
        Long amount = 5000L;
        Point point = pointService.earn(member.getId(), amount, true, 30);
        pointRepository.flush();
        // 포인트 사용
        Long useAmount = 3000L;
        pointService.usePoint(member.getId(), useAmount, "ORDER001");
        pointRepository.flush();

        point.setExpireAt(LocalDateTime.now().minusDays(1)); // 포인트 만료일 경과
        pointRepository.flush();

        // When
        pointService.cancelPointUse(member.getId(), "ORDER001", 3000L);
        pointRepository.flush();

        // Then
        List<Point> availablePoints = pointRepository.availablePointsByMember(member);
        assertEquals(1, availablePoints.size());
        Point newPoint = availablePoints.get(0);

        assertEquals(amount, newPoint.getAvailableAmount());
        assertEquals(0L, newPoint.getUsedAmount());
        assertTrue(newPoint.getExpireAt().isAfter(LocalDateTime.now()));
        assertNull(newPoint.getCanceledAt());
    }
}
