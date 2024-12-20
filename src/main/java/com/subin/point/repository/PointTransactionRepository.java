package com.subin.point.repository;

import com.subin.point.entity.Member;
import com.subin.point.entity.PointTransaction;
import com.subin.point.entity.TransactionType;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PointTransactionRepository extends JpaRepository<PointTransaction, Long> {
    // 주문번호를 이용한 트랜잭션 전체조회
    List<PointTransaction> findByOrderId(String orderId);

    // 이용 타입별 트랜잭션 전체조회
    List<PointTransaction> findAllByType(TransactionType type);

    // 사용자 & 주문번호 & 트랜잭션 타입 조회 (IN 조건)
    @EntityGraph(attributePaths = {"point"})
    List<PointTransaction> findByMemberAndOrderIdAndTypeIn(Member member, String orderId, List<TransactionType> types);

    // 사용자 & 주문번호 & 트랜잭션 타입 조회
    List<PointTransaction> findByMemberAndOrderIdAndType(Member member, String orderId, TransactionType type);

    // 동일 주문번호 존재 여부 조회
    boolean existsByOrderIdAndType(String orderId, TransactionType type);

}
