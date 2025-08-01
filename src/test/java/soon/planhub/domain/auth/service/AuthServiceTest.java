package soon.planhub.domain.auth.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import soon.planhub.IntegrationTestSupport;
import soon.planhub.domain.auth.service.dto.request.ReissueTokenServiceRequest;
import soon.planhub.domain.member.entity.Member;
import soon.planhub.domain.member.repository.MemberRepository;
import soon.planhub.global.exception.dto.ErrorDetail;
import soon.planhub.global.exception.dto.member.InvalidRefreshTokenException;
import soon.planhub.global.security.jwt.dto.response.TokenResponse;
import soon.planhub.global.security.jwt.provider.JwtProvider;

class AuthServiceTest extends IntegrationTestSupport {

    @Autowired
    private AuthService authService;

    @Autowired
    private MemberRepository memberRepository;

    @MockitoBean
    private JwtProvider jwtProvider;

    @AfterEach
    void tearDown() {
        memberRepository.deleteAllInBatch();
    }

    @DisplayName("유효한 리프레시 토큰일 경우 새로운 토큰을 발급한다.")
    @Test
    void reissueToken() {
        // given
        Member member = createMember();
        memberRepository.save(member);

        given(jwtProvider.getSubjectFromToken("accessToken"))
            .willReturn(String.valueOf(member.getId()));

        TokenResponse tokenResponse = new TokenResponse("new AccessToken", "new RefreshToken");
        given(jwtProvider.generateAllToken(member.getId()))
            .willReturn(tokenResponse);

        ReissueTokenServiceRequest request = ReissueTokenServiceRequest.builder()
            .accessToken("accessToken")
            .refreshToken("refreshToken")
            .build();

        // when
        TokenResponse response = authService.reissueToken(request);

        // then
        assertThat(request.accessToken()).isNotEqualTo(response.accessToken());
        assertThat(request.refreshToken()).isNotEqualTo(response.refreshToken());
    }

    @DisplayName("유효하지 않은 리프레시 토큰일 경우 예외를 발생시킨다.")
    @Test
    void reissueTokenWithInvalidRefreshToken() {
        // given
        Member member = createMember();
        memberRepository.save(member);

        given(jwtProvider.getSubjectFromToken("accessToken"))
            .willReturn(String.valueOf(member.getId()));

        ReissueTokenServiceRequest request = ReissueTokenServiceRequest.builder()
            .accessToken("accessToken")
            .refreshToken("invalidRefreshToken")
            .build();

        // expected
        assertThatThrownBy(() -> authService.reissueToken(request))
            .isInstanceOf(InvalidRefreshTokenException.class)
            .hasMessageContaining(ErrorDetail.INVALID_TOKEN.getMessage());
    }

    private Member createMember() {
        Member member = Member.create("test@email", "testNickname", "profileUrl");
        member.setupTokens("refreshToken", "oauthToken");
        return member;
    }

}