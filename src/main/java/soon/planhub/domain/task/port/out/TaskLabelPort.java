package soon.planhub.domain.task.port.out;

import soon.planhub.domain.task.service.dto.label.TaskLabelInformation;

public interface TaskLabelPort {

    void createLabel(TaskLabelInformation info, Long memberId, Long projectId);

}