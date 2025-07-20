package soon.planhub.global.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.METHOD_NOT_ALLOWED;

import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import soon.planhub.global.exception.dto.response.ErrorResponse;

@Slf4j
@RestControllerAdvice
public class GlobalRestExceptionHandler {

    @ExceptionHandler(PlanHubException.class)
    public ResponseEntity<ErrorResponse> planHubException(PlanHubException e) {
        ErrorResponse response = ErrorResponse.of(e);
        log.error("PlanHubException 발생: 상태 코드 - {}, 메시지 - {}", response.status(), response.message(), e);

        return ResponseEntity.status(response.status()).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> exception(Exception e) {
        log.error("예외 발생: {}", e.getMessage(), e);
        ErrorResponse response = ErrorResponse.of(INTERNAL_SERVER_ERROR.value(), "서버 오류가 발생했습니다.");
        return ResponseEntity.status(response.status()).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> invalidRequestHandler(MethodArgumentNotValidException e) {
        Map<String, String> validation = new HashMap<>();
        e.getFieldErrors().forEach(fieldError -> {
            String field = fieldError.getField();
            String errorMessage = fieldError.getDefaultMessage();
            validation.put(fieldError.getField(), fieldError.getDefaultMessage());
            log.warn("유효성 검사 실패 - 필드: {}, 메시지: {}", field, errorMessage);
        });

        ErrorResponse response = ErrorResponse.of(BAD_REQUEST.value(), "잘못된 요청입니다.", validation);
        return ResponseEntity.status(response.status()).body(response);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> missingRequestParameterHandler(MissingServletRequestParameterException e) {
        log.warn("필수 요청 파라미터 누락: {}", e.getParameterName());
        String errorMessage = e.getParameterName() + "는 필수입니다.";
        ErrorResponse response = ErrorResponse.of(BAD_REQUEST.value(), errorMessage);
        return ResponseEntity.status(response.status()).body(response);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> methodNotSupportedHandler(HttpRequestMethodNotSupportedException e) {
        log.warn("지원하지 않는 HTTP 메서드 요청: 현재 메서드 - {}, 지원 메서드 - {}",
            e.getMethod(),
            e.getSupportedHttpMethods()
        );

        String errorMessage = String.format("'%s' 메서드는 지원하지 않습니다.", e.getMethod());
        ErrorResponse response = ErrorResponse.of(METHOD_NOT_ALLOWED.value(), errorMessage);
        return ResponseEntity.status(response.status()).body(response);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> typeMismatchHandler(MethodArgumentTypeMismatchException e) {
        log.warn("요청 파라미터 타입 불일치: 파라미터 - {}, 필요 타입 - {}",
            e.getName(),
            e.getRequiredType().getSimpleName()
        );

        String errorMessage = String.format("'%s' 파라미터는 %s 타입이어야 합니다.",
            e.getName(),
            e.getRequiredType().getSimpleName()
        );
        ErrorResponse response = ErrorResponse.of(BAD_REQUEST.value(), errorMessage);
        return ResponseEntity.status(response.status()).body(response);
    }

}