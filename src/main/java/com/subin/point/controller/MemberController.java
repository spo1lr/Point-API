package com.subin.point.controller;

import com.subin.point.dto.member.CreateMemberRequestDTO;
import com.subin.point.dto.member.CreateMemberResponseDTO;
import com.subin.point.dto.reponse.Code;
import com.subin.point.dto.reponse.Response;
import com.subin.point.entity.Member;
import com.subin.point.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
@RestController
public class MemberController {

    private final MemberService memberService;

    @PostMapping("")
    public ResponseEntity<Response<CreateMemberResponseDTO>> createMember(@RequestBody CreateMemberRequestDTO request) {
        Member member = memberService.createMember(request.getName());
        CreateMemberResponseDTO createMemberDTO = new CreateMemberResponseDTO(member);
        return Response.of(Code.REQUEST_SUCCESS, createMemberDTO);
    }
}
