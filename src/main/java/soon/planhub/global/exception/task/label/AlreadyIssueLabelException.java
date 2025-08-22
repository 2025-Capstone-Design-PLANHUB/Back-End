package soon.planhub.global.exception.task.label;

import soon.planhub.global.exception.PlanHubException;
import soon.planhub.global.exception.dto.ErrorDetail;

public class AlreadyIssueLabelException extends PlanHubException {

    public AlreadyIssueLabelException(ErrorDetail errorDetail) {
        super(errorDetail);
    }

}