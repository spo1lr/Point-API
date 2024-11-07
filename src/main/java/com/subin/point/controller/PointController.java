package com.subin.point.controller;

import com.subin.point.dto.point.*;
import com.subin.point.dto.reponse.Code;
import com.subin.point.dto.reponse.Response;
import com.subin.point.entity.Point;
import com.subin.point.service.PointService;
import jakarta.validation.Valid;
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

    // 포인트 적립
    @PostMapping("/earn")
    public ResponseEntity<Response<EarnResponseDTO>> earnPoint(@Valid  @RequestBody EarnRequestDTO request) {
        Point point = pointService.earn(request.getMemberId(), request.getAmount(), request.isManual(), request.getExpireDays());

        return Response.of(Code.REQUEST_SUCCESS, new EarnResponseDTO(point));
    }

    // 포인트 적립 취소
    @PostMapping("/earn/cancel")
    public ResponseEntity<Response<Void>> cancelEarn(@Valid @RequestBody EarnCancelRequestDTO request) {
        pointService.cancelEarnedPoint(request.getMemberId(), request.getAmount());
        return Response.of(Code.REQUEST_SUCCESS);
    }

    // 포인트 사용
    @PostMapping("/use")
    public ResponseEntity<Response<Void>> usePoint(@Valid @RequestBody UseRequestDTO request) {
        pointService.usePoint(request.getMemberId(), request.getAmount(), request.getOrderId());
        return Response.of(Code.REQUEST_SUCCESS);
    }

    // 포인트 사용 취소
    @PostMapping("/use/cancel")
    public ResponseEntity<Response<Void>> cancelPointUse(@Valid @RequestBody UseCancelRequestDTO request) {
        pointService.cancelPointUse(request.getMemberId(), request.getOrderId(), request.getAmount());
        return Response.of(Code.REQUEST_SUCCESS);
    }
}
