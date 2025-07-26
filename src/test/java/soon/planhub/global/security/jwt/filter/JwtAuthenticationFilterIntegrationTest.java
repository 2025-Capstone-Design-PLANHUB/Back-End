package soon.planhub.global.security.jwt.filter;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static soon.planhub.global.security.jwt.common.TokenType.AUTHORIZATION_HEADER;
import static soon.planhub.global.security.jwt.common.TokenType.BEARER_PREFIX;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import soon.planhub.IntegrationTestSupport;
import soon.planhub.TestSecurityConfig;
import soon.planhub.global.security.jwt.dto.response.TokenResponse;
import soon.planhub.global.security.jwt.provider.JwtProvider;

@Import(TestSecurityConfig.class)
@AutoConfigureMockMvc
public class JwtAuthenticationFilterIntegrationTest extends IntegrationTestSupport {

    private static final String SECURE_API_PATH = "/api/test/secure";
    private static final String PUBLIC_API_PATH = "/api/test/public";
    private static final long MEMBER_ID = 1L;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtProvider jwtProvider;

    @DisplayName("유효한 토큰으로 보호된 API에 접근 할 수 있다.")
    @Test
    void givenValidTokenWhenAccessingSecureApiThenSucceeds() throws Exception {
        // given
        TokenResponse tokenResponse = jwtProvider.generateAllToken(MEMBER_ID);

        // expected
        mockMvc.perform(
                get(SECURE_API_PATH)
                    .header(
                        AUTHORIZATION_HEADER.getValue(),
                        BEARER_PREFIX.getValue() + tokenResponse.accessToken()
                    )
            )
            .andExpect(status().isOk());
    }

    @DisplayName("토큰 없이 인증이 필요한 API를 요청하면 에러를 반환한다.")
    @Test
    void requestWithoutTokenShouldFail() throws Exception {
        // expected
        mockMvc.perform(
                get(SECURE_API_PATH)
            )
            .andExpect(status().isUnauthorized());
    }

    @DisplayName("인증이 필요 없는 API는 토큰 없이도 접근할 수 있다.")
    @Test
    void publicApiShouldBeAccessibleWithoutToken() throws Exception {
        mockMvc.perform(
                get(PUBLIC_API_PATH)
            )
            .andExpect(status().isOk());
    }

    @DisplayName("유효하지 않은 토큰으로 보호된 API에 접근하면 에러를 반환한다.")
    @Test
    void givenInvalidTokenWhenAccessingSecureApiThenFails() throws Exception {
        // given
        String invalidToken = "this-is-an-invalid-token";

        // expected
        mockMvc.perform(
                get(SECURE_API_PATH)
                    .header(AUTHORIZATION_HEADER.getValue(), BEARER_PREFIX.getValue() + invalidToken)
            )
            .andExpect(status().isUnauthorized());
    }

    @DisplayName("Bearer 접두사 없는 토큰으로 보호된 API에 접근하면 에러를 반환한다.")
    @Test
    void givenTokenWithoutBearerPrefixWhenAccessingSecureApiThenFails() throws Exception {
        // given
        TokenResponse tokenResponse = jwtProvider.generateAllToken(MEMBER_ID);

        // expected
        mockMvc.perform(
                get(SECURE_API_PATH)
                    .header(
                        AUTHORIZATION_HEADER.getValue(),
                        tokenResponse.accessToken()
                    )
            )
            .andExpect(status().isUnauthorized());
    }

    @DisplayName("잘못된 서명 토큰으로 보호된 API에 접근하면 에러를 반환한다.")
    @Test
    void givenInvalidSignatureTokenWhenAccessingSecureApiThenFails() throws Exception {
        // given
        String invalidSignatureToken = "invalid-signature-token";

        // expected
        mockMvc.perform(
                get(SECURE_API_PATH)
                    .header(
                        AUTHORIZATION_HEADER.getValue(),
                        BEARER_PREFIX.getValue() + invalidSignatureToken
                    )
            )
            .andExpect(status().isUnauthorized());
    }

    @DisplayName("Authorization 헤더가 비어 있으면 에러를 반환한다.")
    @Test
    void givenEmptyAuthorizationHeaderWhenAccessingSecureApiThenFails() throws Exception {
        // expected
        mockMvc.perform(
                get(SECURE_API_PATH)
                    .header(AUTHORIZATION_HEADER.getValue(), "")
            )
            .andExpect(status().isUnauthorized());
    }

    @DisplayName("Authorization 헤더가 없으면 에러를 반환한다.")
    @Test
    void givenNoAuthorizationHeaderWhenAccessingSecureApiThenFails() throws Exception {
        // expected
        mockMvc.perform(
                get(SECURE_API_PATH)
            )
            .andExpect(status().isUnauthorized());
    }

    @DisplayName("잘못된 형식의 Authorization 헤더로 보호된 API에 접근하면 에러를 반환한다.")
    @Test
    void givenMalformedAuthorizationHeaderWhenAccessingSecureApiThenFails() throws Exception {
        // given
        String invalidHeader = "InvalidHeaderFormat";

        // expected
        mockMvc.perform(
                get(SECURE_API_PATH)
                    .header(AUTHORIZATION_HEADER.getValue(), invalidHeader)
            )
            .andExpect(status().isUnauthorized());
    }

}