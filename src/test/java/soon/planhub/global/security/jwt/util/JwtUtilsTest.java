package soon.planhub.global.security.jwt.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class JwtUtilsTest {

    @Test
    @DisplayName("유효한 Bearer 토큰 헤더에서 토큰을 성공적으로 추출한다")
    void getTokenFromHeaderWithValidBearerTokenShouldReturnToken() {
        // given
        String headerValue = "Bearer token";

        // when
        Optional<String> result = JwtUtils.getTokenFromHeader(headerValue);

        // then
        assertThat(result)
            .isPresent()
            .contains("token");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"  ", "my-jwt-token", "bearer my-jwt-token"})
    @DisplayName("유효하지 않은 헤더 값에 대해 빈 Optional을 반환한다")
    void getTokenFromHeaderWithInvalidHeaderShouldReturnEmpty(String headerValue) {
        // when
        Optional<String> result = JwtUtils.getTokenFromHeader(headerValue);

        // then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Bearer 접두사만 있고 토큰 값이 없는 경우 빈 문자열을 포함한 Optional을 반환한다")
    void getTokenFromHeaderWithOnlyBearerPrefixShouldReturnEmptyString() {
        // given
        String headerValue = "Bearer ";

        // when
        Optional<String> result = JwtUtils.getTokenFromHeader(headerValue);

        // then
        assertThat(result)
            .isPresent()
            .contains("");
    }

}