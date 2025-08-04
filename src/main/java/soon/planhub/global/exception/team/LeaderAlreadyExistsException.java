package soon.planhub.global.exception.team;

import soon.planhub.global.exception.PlanHubException;
import soon.planhub.global.exception.dto.ErrorDetail;

public class LeaderAlreadyExistsException extends PlanHubException {

    public LeaderAlreadyExistsException(ErrorDetail errorDetail) {
        super(errorDetail);
    }

}