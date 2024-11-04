package com.subin.point.controller;

import com.subin.point.dto.EarnCancelRequestDTO;
import com.subin.point.dto.EarnRequestDTO;
import com.subin.point.dto.UseCancelRequestDTO;
import com.subin.point.dto.UseRequestDTO;
import com.subin.point.entity.Point;
import com.subin.point.service.PointService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/points")
@RestController
public class PointController {

    private final PointService pointService;

    // 포인트 지급
    @PostMapping("/earn")
    public ResponseEntity<Point> earnPoint(@RequestBody EarnRequestDTO request) {
        Point point = pointService.earn(request.getMemberId(), request.getAmount(), request.isManual(), request.getExpireDays());
        return ResponseEntity.ok(point);
    }

    // 포인트 지급 취소
    @PostMapping("/earn/cancel")
    public ResponseEntity<Void> cancelEarn(@RequestBody EarnCancelRequestDTO request) {
        pointService.cancelEarnedPoint(request.getMemberId(), request.getAmount());
        return ResponseEntity.ok().build();
    }

    // 포인트 사용
    @PostMapping("/use")
    public ResponseEntity<Void> usePoint(@RequestBody UseRequestDTO request) {
        pointService.usePoint(request.getMemberId(), request.getAmount(), request.getOrderId());
        return ResponseEntity.ok().build();
    }

    // 포인트 사용 취소
    @PostMapping("/use/cancel")
    public ResponseEntity<Void> cancelPointUse(@RequestBody UseCancelRequestDTO request) {
        pointService.cancelPointUse(request.getMemberId(), request.getOrderId(), request.getAmount());
        return ResponseEntity.ok().build();
    }
}
