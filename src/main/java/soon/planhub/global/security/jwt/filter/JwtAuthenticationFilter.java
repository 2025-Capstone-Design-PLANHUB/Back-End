package soon.planhub.global.security.jwt.filter;

import static jakarta.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static soon.planhub.global.exception.dto.ErrorDetail.INVALID_TOKEN;
import static soon.planhub.global.security.jwt.common.TokenType.AUTHORIZATION_HEADER;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import soon.planhub.global.exception.dto.response.ErrorResponse;
import soon.planhub.global.security.jwt.provider.JwtProvider;
import soon.planhub.global.security.jwt.util.JwtUtils;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(
        @NonNull HttpServletRequest request,
        @NonNull HttpServletResponse response,
        @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            getAuthenticationFromRequest(request).ifPresent(auth ->
                SecurityContextHolder.getContext()
                    .setAuthentication(auth)
            );
        } catch (Exception e) {
            log.error("JWT 인증 실패: ", e);
            responseJWTError(response);
            return;
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        return path.equals("/api/v1/auth/reissue");
    }

    private Optional<Authentication> getAuthenticationFromRequest(HttpServletRequest request) {
        String header = request.getHeader(AUTHORIZATION_HEADER.getValue());
        return JwtUtils.getTokenFromHeader(header)
            .filter(jwtProvider::validateToken)
            .map(jwtProvider::getAuthentication);
    }

    private void responseJWTError(HttpServletResponse response) {
        response.setStatus(SC_UNAUTHORIZED);
        response.setContentType(APPLICATION_JSON_VALUE);
        try {
            ErrorResponse errorResponse = ErrorResponse.of(
                SC_UNAUTHORIZED,
                INVALID_TOKEN.getMessage()
            );
            String jsonResponse = objectMapper.writeValueAsString(errorResponse);
            response.getWriter()
                .write(jsonResponse);
        } catch (IOException e) {
            log.error("JWT 에러 응답 생성 실패", e);
        }
    }

}