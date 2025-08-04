package soon.planhub.global.exception.common;

import soon.planhub.global.exception.PlanHubException;
import soon.planhub.global.exception.dto.ErrorDetail;

public class UnauthorizedException extends PlanHubException {

    public UnauthorizedException(ErrorDetail errorDetail) {
        super(errorDetail);
    }

}