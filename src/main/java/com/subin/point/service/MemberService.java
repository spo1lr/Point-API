package com.subin.point.service;

import com.subin.point.dto.reponse.Code;
import com.subin.point.entity.Member;
import com.subin.point.exception.MemberServiceException;
import com.subin.point.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.subin.point.dto.reponse.Code.NOT_FOUND_MEMBER;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    // 회원 생성
    @Transactional
    public Member createMember(String name) {

        // 중복 닉네임 검증
        if (memberRepository.findFirstByName(name).isPresent()) {
            throw new MemberServiceException(Code.DUPLICATE_NAME);
        }

        return memberRepository.save(Member.createMember(name));
    }

    // 회원 정보 변경 - 1회 최대 적립 포인트, 최대 보유 포인트
    @Transactional
    public void updateMember(Long memberId, Long maxEarnPoint, Long maxHoldPoint) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new MemberServiceException(NOT_FOUND_MEMBER));

        // 회원 적립, 보유 포인트 설정 변경
        member.updateMemberPointSettings(maxEarnPoint, maxHoldPoint);
    }
}
