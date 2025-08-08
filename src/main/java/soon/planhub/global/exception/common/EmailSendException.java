package soon.planhub.global.exception.common;

import soon.planhub.global.exception.PlanHubException;
import soon.planhub.global.exception.dto.ErrorDetail;

public class EmailSendException extends PlanHubException {

    public EmailSendException(ErrorDetail errorDetail) {
        super(errorDetail);
    }

}