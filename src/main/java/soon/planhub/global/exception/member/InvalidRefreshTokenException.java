package soon.planhub.global.exception.member;

import soon.planhub.global.exception.PlanHubException;
import soon.planhub.global.exception.dto.ErrorDetail;

public class InvalidRefreshTokenException extends PlanHubException {

    public InvalidRefreshTokenException(ErrorDetail errorDetail) {
        super(errorDetail);
    }

}