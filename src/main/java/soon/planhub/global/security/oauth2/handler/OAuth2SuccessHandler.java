package soon.planhub.global.security.oauth2.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import soon.planhub.domain.member.service.MemberService;
import soon.planhub.global.security.jwt.dto.response.TokenResponse;
import soon.planhub.global.security.jwt.provider.JwtProvider;
import soon.planhub.global.security.oauth2.dto.CustomOAuth2Member;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private static final String BASE_URL = "http://localhost:3000";
    private final JwtProvider jwtProvider;
    private final MemberService memberService;

    @Override
    public void onAuthenticationSuccess(
        HttpServletRequest request,
        HttpServletResponse response,
        Authentication authentication
    ) throws IOException {
        CustomOAuth2Member oAuth2Member = (CustomOAuth2Member) authentication.getPrincipal();
        TokenResponse tokenResponse = jwtProvider.generateAllToken(oAuth2Member.getMemberId());

        memberService.updateMemberTokens(
            oAuth2Member.getMemberId(),
            oAuth2Member.getOauthAccessToken(),
            tokenResponse.refreshToken()
        );

        String redirectUrl = buildRedirectUrl(tokenResponse);
        getRedirectStrategy().sendRedirect(request, response, redirectUrl);

        log.info("OAuth2 인증 성공 - nickname: {}, 토큰 발행 완료{}", oAuth2Member.getNickname(),
            oAuth2Member.getOauthAccessToken());
    }

    private String buildRedirectUrl(TokenResponse tokenResponse) {
        return UriComponentsBuilder.fromUriString(BASE_URL + "/oauth2/redirect")
            .queryParam("accessToken", tokenResponse.accessToken())
            .queryParam("refreshToken", tokenResponse.refreshToken())
            .build()
            .toUriString();
    }

}