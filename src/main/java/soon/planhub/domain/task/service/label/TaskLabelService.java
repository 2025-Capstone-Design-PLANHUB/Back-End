package soon.planhub.domain.task.service.label;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import soon.planhub.domain.task.port.out.TaskLabelPort;
import soon.planhub.domain.task.service.dto.label.request.TaskLabelCreateServiceRequest;

@RequiredArgsConstructor
@Service
public class TaskLabelService {

    private final TaskLabelCreator taskLabelCreator;
    private final TaskLabelValidator taskLabelValidator;
    private final TaskLabelPort taskLabelPort;

    public Long createLabel(TaskLabelCreateServiceRequest request, Long memberId) {
        taskLabelValidator.validateLabelNotExists(request.title(), request.projectId());

        taskLabelPort.createLabel(request.toInfo(), memberId, request.projectId()); // github API 호출
        return taskLabelCreator.createLabel(request.toInfo(), request.projectId());
    }

}