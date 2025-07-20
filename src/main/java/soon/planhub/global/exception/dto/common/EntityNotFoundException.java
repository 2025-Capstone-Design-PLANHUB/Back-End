package soon.planhub.global.exception.dto.common;

import soon.planhub.global.exception.PlanHubException;
import soon.planhub.global.exception.dto.ErrorDetail;

public class EntityNotFoundException extends PlanHubException {

    public EntityNotFoundException(ErrorDetail errorDetail) {
        super(errorDetail);
    }

}