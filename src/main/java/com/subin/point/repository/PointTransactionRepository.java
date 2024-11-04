package com.subin.point.repository;

import com.subin.point.entity.PointTransaction;
import com.subin.point.entity.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PointTransactionRepository extends JpaRepository<PointTransaction, Long> {
    // 주문번호를 이용한 트랜잭션 전체조회
    List<PointTransaction> findByOrderId(String orderId);

    // 이용 타입별 트랜잭션 전체조회
    List<PointTransaction> findAllByType(TransactionType type);
}
