package soon.planhub.domain.member.service;

import static org.assertj.core.api.Assertions.assertThat;
import static soon.springtestutil.querycount.assertion.QueryCounterAssertion.assertCounts;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import soon.planhub.IntegrationTestSupport;
import soon.planhub.domain.member.entity.Member;
import soon.planhub.domain.member.repository.MemberRepository;

class MemberServiceTest extends IntegrationTestSupport {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @AfterEach
    void tearDown() {
        memberRepository.deleteAllInBatch();
    }

    @DisplayName("회원의 토큰 정보를 업데이트한다.")
    @Test
    void updatesMemberTokens() {
        // given
        Member member = Member.create("test@email", "testNickname", "testProfileImageUrl");
        memberRepository.save(member);

        // when
        memberService.updateMemberTokens(member.getId(), "newAccessToken", "newRefreshToken");

        // then
        Member updatedMember = memberRepository.findById(member.getId());
        assertThat(updatedMember)
            .extracting("oauthToken", "refreshToken")
            .containsExactly("newAccessToken", "newRefreshToken");

        assertCounts()
            .select(2)
            .update(1)
            .verify();
    }

}