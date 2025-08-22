package soon.planhub.domain.task.port.out;

import soon.planhub.domain.task.service.dto.label.TaskLabelInformation;
import soon.planhub.domain.task.service.dto.label.request.TaskLabelUpdateServiceRequest;

public interface TaskLabelPort {

    void createLabel(TaskLabelInformation info, Long memberId, Long projectId);

    void updateLabel(TaskLabelUpdateServiceRequest request, Long memberId);

}