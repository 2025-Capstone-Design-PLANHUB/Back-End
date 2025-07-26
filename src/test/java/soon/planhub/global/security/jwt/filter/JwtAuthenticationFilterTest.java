package soon.planhub.global.security.jwt.filter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static soon.planhub.global.security.jwt.common.TokenType.AUTHORIZATION_HEADER;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import java.io.IOException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import soon.planhub.global.security.jwt.provider.JwtProvider;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Mock
    private JwtProvider jwtProvider;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private FilterChain filterChain;

    @Mock
    private Authentication authentication;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    void setUp() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @DisplayName("유효한 토큰이 헤더에 존재하면 인증 객체를 컨텍스트에 저장한다.")
    @Test
    void doFilterInternalWithValidTokenShouldSetAuthentication()
        throws ServletException, IOException {
        // given
        String token = "valid-token";
        request.addHeader("Authorization", "Bearer " + token);

        given(jwtProvider.validateToken(token)).willReturn(true);
        given(jwtProvider.getAuthentication(token)).willReturn(authentication);

        // when
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // then
        assertThat(SecurityContextHolder.getContext().getAuthentication())
            .isEqualTo(authentication);
        verify(filterChain).doFilter(request, response);
    }

    @DisplayName("토큰이 존재하지 않다면 인증 객체를 저장하지 않고 다음 필터를 호출한다.")
    @Test
    void withoutTokenShouldNotSetAuthenticationAndCallNextFilter()
        throws ServletException, IOException {
        // given
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // expected
        assertThat(SecurityContextHolder.getContext().getAuthentication())
            .isNull();
        verify(filterChain).doFilter(request, response);
        verify(jwtProvider, never()).validateToken(anyString());
    }

    @Test
    @DisplayName("토큰 유효성 검증이 false를 반환하면 인증 객체 저장 없이 다음 필터를 호출한다")
    void withInvalidTokenShouldNotSetAuthenticationAndCallNextFilter()
        throws ServletException, IOException {
        // given
        String token = "invalid-token";
        request.addHeader(AUTHORIZATION_HEADER.getValue(), "Bearer " + token);

        given(jwtProvider.validateToken(token)).willReturn(false);

        // when
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // then
        assertThat(SecurityContextHolder.getContext().getAuthentication())
            .isNull();
        verify(filterChain).doFilter(request, response);
        verify(jwtProvider, never()).getAuthentication(anyString());
    }

    @Test
    @DisplayName("토큰 처리 중 예외가 발생하면 401 에러를 응답하고 필터 체인을 중단한다")
    void whenTokenProcessingThrowsException_shouldSendErrorAndStopChain()
        throws ServletException, IOException {
        // given
        String token = "exception-token";
        request.addHeader(AUTHORIZATION_HEADER.getValue(), "Bearer " + token);

        given(jwtProvider.validateToken(token))
            .willThrow(new JwtException("Token processing failed"));
        given(objectMapper.writeValueAsString(any())).willReturn("{}");

        // when
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // then
        assertThat(SecurityContextHolder.getContext().getAuthentication())
            .isNull();
        assertThat(response.getStatus()).isEqualTo(401);
        verify(filterChain, never()).doFilter(request, response);
    }

}