package soon.planhub.global.security.jwt.handler;

import static jakarta.servlet.http.HttpServletResponse.SC_FORBIDDEN;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(
        HttpServletRequest request,
        HttpServletResponse response,
        AccessDeniedException accessDeniedException
    ) throws IOException, ServletException {
        log.info("접근 권한 없음: 요청한 리소스에 대한 권한이 없습니다");
        response.sendError(SC_FORBIDDEN);
    }

}