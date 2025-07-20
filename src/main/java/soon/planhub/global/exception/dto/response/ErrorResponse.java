package soon.planhub.global.exception.dto.response;

import java.util.Map;
import soon.planhub.global.exception.PlanHubException;

/**
 * { "code": "400", "message": "잘못된 요청입니다.", "validation": { "title": "값을 입력해주세요" } }
 */

public record ErrorResponse(

    int status,
    String message,
    Map<String, String> validation

) {

    public ErrorResponse(int status, String message, Map<String, String> validation) {
        this.status = status;
        this.message = message;
        this.validation = (validation != null) ? validation : Map.of();
    }

    public static ErrorResponse of(PlanHubException e) {
        return new ErrorResponse(
            e.getErrorDetail().getStatus(),
            e.getMessage(),
            e.getValidation()
        );
    }

    public static ErrorResponse of(int status, String message, Map<String, String> validation) {
        return new ErrorResponse(status, message, validation);
    }

    public static ErrorResponse of(int status, String message) {
        return new ErrorResponse(status, message, null);
    }

}