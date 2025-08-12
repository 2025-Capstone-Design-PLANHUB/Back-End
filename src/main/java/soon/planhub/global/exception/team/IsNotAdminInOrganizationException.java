package soon.planhub.global.exception.team;

import soon.planhub.global.exception.PlanHubException;
import soon.planhub.global.exception.dto.ErrorDetail;

public class IsNotAdminInOrganizationException extends PlanHubException {

    public IsNotAdminInOrganizationException(ErrorDetail errorDetail) {
        super(errorDetail);
    }

}