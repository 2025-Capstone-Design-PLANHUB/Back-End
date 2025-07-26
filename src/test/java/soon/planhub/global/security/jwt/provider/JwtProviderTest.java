package soon.planhub.global.security.jwt.provider;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static soon.planhub.domain.member.entity.Role.ROLE_USER;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import soon.planhub.global.security.jwt.dto.response.TokenResponse;

class JwtProviderTest {

    private JwtProvider jwtProvider;

    private static final String TEST_SECRET_KEY = "dGhpcy1pcy1hLXN1cGVyLWxvbmctYW5kLXNlY3VyZS1zZWNyZXQta2V5LWZvci10ZXN0aW5nLWhzNTEyLWFsdG9yaXRobS0xMjM0NQ==";
    private static final String WRONG_SECRET_KEY = "dGhpcy1pcy1hLXN1cGVyLWxvbmctYW5kaaaaaaaaaaaaWNyZXQta2V5LWZvci10ZXN0aW5nLWhzNTEyLWFsdG9yaXRobS0xMjM0NQ==";

    @BeforeEach
    void setUp() {
        jwtProvider = new JwtProvider(TEST_SECRET_KEY);
    }

    @DisplayName("액세스 토큰과 리프레시 토큰을 생성한다.")
    @Test
    void generateAllToken() {
        // given
        Long memberId = 1L;

        // when
        TokenResponse tokenResponse = jwtProvider.generateAllToken(memberId);

        // then
        assertThat(tokenResponse).isNotNull();
        assertThat(tokenResponse.accessToken()).isNotBlank();
        assertThat(tokenResponse.refreshToken()).isNotBlank();
    }

    @DisplayName("액세스 토큰의 subject를 가져온다.")
    @Test
    void getSubjectFromToken() {
        // given
        Long memberId = 1L;
        TokenResponse tokenResponse = jwtProvider.generateAllToken(memberId);

        // when
        String subjectFromToken = jwtProvider.getSubjectFromToken(tokenResponse.accessToken());

        // then
        assertThat(subjectFromToken).isEqualTo(String.valueOf(memberId));
    }

    @DisplayName("토큰으로 Authentication 객체를 생성한다.")
    @Test
    void getAuthentication() {
        // given
        Long memberId = 1L;
        TokenResponse tokenResponse = jwtProvider.generateAllToken(memberId);
        String accessToken = tokenResponse.accessToken();

        // when
        Authentication authentication = jwtProvider.getAuthentication(accessToken);

        // then
        assertThat(authentication.getName()).isEqualTo(String.valueOf(memberId));
        assertThat(authentication.getAuthorities())
            .extracting(GrantedAuthority::getAuthority)
            .containsExactly(ROLE_USER.name());
    }

    @DisplayName("유효한 토큰을 검증한다.")
    @Test
    void validateTokenValidToken() {
        // given
        Long memberId = 1L;
        TokenResponse tokenResponse = jwtProvider.generateAllToken(memberId);
        String validToken = tokenResponse.accessToken();

        // when
        boolean isValid = jwtProvider.validateToken(validToken);

        // then
        assertThat(isValid).isTrue();
    }

    @DisplayName("유효하지 않은 토큰을 검증한다.")
    @Test
    void validateTokenInvalidToken() {
        // given
        String invalidToken = "invalid.token.value";

        // when
        boolean isValid = jwtProvider.validateToken(invalidToken);

        // then
        assertThat(isValid).isFalse();
    }

    @Test
    @DisplayName("만료된 토큰을 검증한다.")
    void expiredTokenTest() {
        // given
        long memberId = 1L;
        Date expiration = new Date(System.currentTimeMillis() - 1000);
        String expiredToken = createToken(memberId, expiration, TEST_SECRET_KEY);

        // when
        boolean result = jwtProvider.validateToken(expiredToken);

        // then
        assertThat(result).isFalse();
    }


    @DisplayName("잘못된 서명을 가진 토큰을 검증한다.")
    @Test
    void validateTokenWithWrongSignature() {
        // given
        long memberId = 1L;
        Date expiration = new Date(System.currentTimeMillis() + 10000);
        String tokenWithWrongSignature = createToken(memberId, expiration, WRONG_SECRET_KEY);

        // when
        boolean isValid = jwtProvider.validateToken(tokenWithWrongSignature);

        // then
        assertThat(isValid).isFalse();
    }

    @DisplayName("auth 클레임이 없는 토큰으로 Authentication 객체를 생성하면 예외가 발생한다.")
    @Test
    void getAuthenticationWithoutAuthClaim() {
        // given
        long memberId = 1L;
        String tokenWithoutAuth = createTokenWithoutAuthClaim(memberId,
            new Date(System.currentTimeMillis() + 10000));

        // expected
        assertThatThrownBy(() -> jwtProvider.getAuthentication(tokenWithoutAuth))
            .isInstanceOf(RuntimeException.class);
    }

    private String createToken(Long memberId, Date expiration, String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        Key key = Keys.hmacShaKeyFor(keyBytes);

        return Jwts.builder()
            .setSubject(String.valueOf(memberId))
            .claim("auth", ROLE_USER.name())
            .setExpiration(expiration)
            .setIssuedAt(new Date())
            .signWith(key, SignatureAlgorithm.HS512)
            .compact();
    }

    private String createTokenWithoutAuthClaim(Long memberId, Date expiration) {
        byte[] keyBytes = Decoders.BASE64.decode(TEST_SECRET_KEY);
        Key key = Keys.hmacShaKeyFor(keyBytes);

        return Jwts.builder()
            .setSubject(String.valueOf(memberId))
            .setExpiration(expiration)
            .setIssuedAt(new Date())
            .signWith(key, SignatureAlgorithm.HS512)
            .compact();
    }

}