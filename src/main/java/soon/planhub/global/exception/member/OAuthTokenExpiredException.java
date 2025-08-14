package soon.planhub.global.exception.member;

import soon.planhub.global.exception.PlanHubException;
import soon.planhub.global.exception.dto.ErrorDetail;

public class OAuthTokenExpiredException extends PlanHubException {

    public OAuthTokenExpiredException(ErrorDetail errorDetail) {
        super(errorDetail);
    }

}