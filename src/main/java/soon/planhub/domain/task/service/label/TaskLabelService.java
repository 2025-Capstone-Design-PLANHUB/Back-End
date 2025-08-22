package soon.planhub.domain.task.service.label;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import soon.planhub.domain.task.port.out.TaskLabelPort;
import soon.planhub.domain.task.service.dto.label.request.TaskLabelCreateServiceRequest;
import soon.planhub.domain.task.service.dto.label.request.TaskLabelUpdateServiceRequest;

@RequiredArgsConstructor
@Service
public class TaskLabelService {

    private final TaskLabelCreator taskLabelCreator;
    private final TaskLabelModifier taskLabelModifier;
    private final TaskLabelValidator taskLabelValidator;
    private final TaskLabelPort taskLabelPort;

    public Long createLabel(TaskLabelCreateServiceRequest request, Long memberId) {
        taskLabelValidator.validateLabelNotExists(request.title(), request.projectId());

        taskLabelPort.createLabel(request.toInfo(), memberId, request.projectId()); // github API 호출
        return taskLabelCreator.createLabel(request.toInfo(), request.projectId());
    }

    public void updateLabel(TaskLabelUpdateServiceRequest request, Long memberId) {
        if (!request.oldTitle().equals(request.newTitle())) {
            taskLabelValidator.validateLabelNotExists(request.newTitle(), request.projectId());
        }

        taskLabelModifier.updateLabel(request.toInfo(), request.labelId());
        taskLabelPort.updateLabel(request, memberId); // github API 호출
    }

}