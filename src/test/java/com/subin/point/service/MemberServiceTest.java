package com.subin.point.service;

import com.subin.point.dto.reponse.Code;
import com.subin.point.entity.Member;
import com.subin.point.exception.MemberServiceException;
import com.subin.point.repository.MemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
public class MemberServiceTest {
    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberRepository memberRepository;

    @AfterEach
    public void tearDown() {
        memberRepository.deleteAllInBatch();
    }

    @Test
    public void 회원_생성_테스트() {
        // Given
        String name = "테스트";

        // When
        Member createdMember = memberService.createMember(name);
        memberRepository.flush();

        // Then
        Member foundMember = memberRepository.findById(createdMember.getId()).orElseThrow();
        assertNotNull(foundMember);
        assertEquals(name, foundMember.getName());
    }

    @Test
    public void 중복_회원_생성_테스트() {
        // Given
        String name = "테스트";
        memberService.createMember(name); // 중복 이름으로 첫 번째 회원 생성
        memberRepository.flush();

        // When & Then
        MemberServiceException exception = assertThrows(MemberServiceException.class, () -> {
            memberService.createMember(name); // 중복된 이름으로 두 번째 회원 생성 시도
            memberRepository.flush();
        });
        assertEquals(Code.DUPLICATE_NAME, exception.getCode());
    }

    @Test
    public void 회원_포인트_설정_변경_테스트() {
        // Given
        Long maxEarnPoint = 1000L;
        Long maxHoldPoint = 5000L;
        Member member = Member.createMember("테스트");
        memberRepository.save(member);
        memberRepository.flush();

        // When
        memberService.updateMember(member.getId(), maxEarnPoint, maxHoldPoint);
        memberRepository.flush();

        // Then
        Member updatedMember = memberRepository.findById(member.getId()).orElseThrow();

        assertEquals(maxEarnPoint, updatedMember.getMaxEarnPoint());
        assertEquals(maxHoldPoint, updatedMember.getMaxHoldPoint());
    }
}
