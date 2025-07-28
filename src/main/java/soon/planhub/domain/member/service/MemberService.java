package soon.planhub.domain.member.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import soon.planhub.domain.member.entity.Member;
import soon.planhub.domain.member.repository.MemberRepository;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public void updateMemberTokens(Long memberId, String oauthAccessToken, String refreshToken) {
        Member member = memberRepository.findById(memberId);
        member.setupTokens(refreshToken, oauthAccessToken);
    }

}