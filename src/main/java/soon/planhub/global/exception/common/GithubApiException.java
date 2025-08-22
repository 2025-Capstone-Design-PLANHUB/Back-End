package soon.planhub.global.exception.common;

import soon.planhub.global.exception.PlanHubException;
import soon.planhub.global.exception.dto.ErrorDetail;

public class GithubApiException extends PlanHubException {

    public GithubApiException(ErrorDetail errorDetail) {
        super(errorDetail);
    }

    public GithubApiException(ErrorDetail errorDetail, Throwable cause) {
        super(errorDetail, cause);
    }

}
