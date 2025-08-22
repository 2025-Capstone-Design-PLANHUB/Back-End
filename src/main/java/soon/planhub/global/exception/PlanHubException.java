package soon.planhub.global.exception;

import lombok.Getter;
import soon.planhub.global.exception.dto.ErrorDetail;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class PlanHubException extends RuntimeException {

    private final Map<String, String> validation;
    private final ErrorDetail errorDetail;

    public PlanHubException(ErrorDetail errorDetail) {
        super(errorDetail.getMessage());
        this.errorDetail = errorDetail;
        this.validation = new HashMap<>();
    }

    public PlanHubException(ErrorDetail errorDetail, Throwable cause) {
        super(errorDetail.getMessage(), cause);
        this.errorDetail = errorDetail;
        this.validation = new HashMap<>();
    }

    public void addValidation(String fieldName, String message) {
        validation.put(fieldName, message);
    }

    public Map<String, String> getValidation() {
        return Collections.unmodifiableMap(validation);
    }

}