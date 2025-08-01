package soon.planhub.global.exception.common;

import lombok.Getter;
import soon.planhub.global.exception.PlanHubException;
import soon.planhub.global.exception.dto.ErrorDetail;

@Getter
public class InvalidRequest extends PlanHubException {

    public InvalidRequest() {
        super(ErrorDetail.INVALID_REQUEST);
    }

    public InvalidRequest(String fieldName, String message) {
        super(ErrorDetail.INVALID_REQUEST); // 에러의 전체적 유형
        addValidation(fieldName, message); // 구체적 에러 메시지
    }

}