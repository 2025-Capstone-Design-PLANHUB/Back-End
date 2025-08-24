package soon.planhub.domain.task.port.out;

import soon.planhub.domain.task.service.dto.label.TaskLabelInformation;
import soon.planhub.domain.task.service.dto.label.request.TaskLabelUpdateServiceRequest;
import soon.planhub.domain.task.service.dto.label.response.TaskLabelDetailResponse;

import java.util.List;

public interface TaskLabelPort {

    void createLabel(TaskLabelInformation info, Long memberId, Long projectId);

    void updateLabel(TaskLabelUpdateServiceRequest request, Long memberId);

    void deleteLabel(Long memberId, Long projectId, String title);

    List<TaskLabelDetailResponse> getLabels(Long memberId, Long projectId);

}