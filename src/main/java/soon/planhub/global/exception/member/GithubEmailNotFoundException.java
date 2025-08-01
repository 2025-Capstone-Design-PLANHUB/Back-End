package soon.planhub.global.exception.member;

import soon.planhub.global.exception.PlanHubException;
import soon.planhub.global.exception.dto.ErrorDetail;

public class GithubEmailNotFoundException extends PlanHubException {

    public GithubEmailNotFoundException(ErrorDetail errorDetail) {
        super(errorDetail);
    }

}