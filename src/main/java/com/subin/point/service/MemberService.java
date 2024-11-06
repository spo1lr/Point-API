package com.subin.point.service;

import com.subin.point.dto.reponse.Code;
import com.subin.point.entity.Member;
import com.subin.point.exception.MemberServiceException;
import com.subin.point.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
