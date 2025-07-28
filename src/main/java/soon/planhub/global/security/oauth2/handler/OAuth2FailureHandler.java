package soon.planhub.global.security.oauth2.handler;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import soon.planhub.global.exception.dto.response.ErrorResponse;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2FailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationFailure(
        HttpServletRequest request,
        HttpServletResponse response,
        AuthenticationException exception
    ) throws IOException, ServletException {
        log.info("OAuth2 인증 실패: {}", exception.getMessage());
        ErrorResponse errorResponse = ErrorResponse.of(UNAUTHORIZED.value(),
            exception.getMessage());

        response.setStatus(UNAUTHORIZED.value());
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(UTF_8.name());

        String json = objectMapper.writeValueAsString(errorResponse);
        response.getWriter().write(json);
    }

}