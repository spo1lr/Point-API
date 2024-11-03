package com.subin.point.controller;

import com.subin.point.dto.EarnRequestDTO;
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

    @PostMapping("/earn")
    public ResponseEntity<Point> earnPoint(@RequestBody EarnRequestDTO request) {
        Point point = pointService.earn(request.getMemberId(), request.getAmount(), request.isManual(), request.getExpireDays());
        return ResponseEntity.ok(point);
    }

    @PostMapping("/use")
    public ResponseEntity<Void> usePoint(@RequestBody UseRequestDTO request) {
        pointService.usePoint(request.getMemberId(), request.getAmount(), request.getOrderId());
        return ResponseEntity.ok().build();
    }
}
