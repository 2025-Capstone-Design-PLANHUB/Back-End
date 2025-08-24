package soon.planhub.domain.task.service.label;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import soon.planhub.domain.task.entity.TaskLabel;
import soon.planhub.domain.task.port.out.TaskLabelPort;
import soon.planhub.domain.task.service.dto.label.request.TaskLabelCreateServiceRequest;
import soon.planhub.domain.task.service.dto.label.request.TaskLabelUpdateServiceRequest;
import soon.planhub.global.annotation.TeamMembership;

@RequiredArgsConstructor
@Service
public class TaskLabelService {

    private final TaskLabelCreator taskLabelCreator;
    private final TaskLabelModifier taskLabelModifier;
    private final TaskLabelRemover taskLabelRemover;
    private final TaskLabelReader taskLabelReader;
    private final TaskLabelValidator taskLabelValidator;
    private final TaskLabelPort taskLabelPort;

    @TeamMembership
    public Long createLabel(Long teamId, Long memberId, TaskLabelCreateServiceRequest request) {
        taskLabelValidator.validateLabelNotExists(request.title(), request.projectId());

        taskLabelPort.createLabel(request.toInfo(), memberId, request.projectId()); // github API 호출
        return taskLabelCreator.createLabel(request.toInfo(), request.projectId());
    }

    @TeamMembership
    public void updateLabel(Long teamId, Long memberId, TaskLabelUpdateServiceRequest request) {
        if (!request.oldTitle().equals(request.newTitle())) {
            taskLabelValidator.validateLabelNotExists(request.newTitle(), request.projectId());
        }

        taskLabelModifier.updateLabel(request.toInfo(), request.labelId());
        taskLabelPort.updateLabel(request, memberId); // github API 호출
    }

    @TeamMembership
    public void deleteLabel(Long teamId, Long memberId, Long labelId) {
        TaskLabel label = taskLabelReader.getLabelById(labelId);
        taskLabelRemover.deleteLabel(label.getId());
        taskLabelPort.deleteLabel(memberId, label.getProject().getId(), label.getTitle()); // github API 호출
    }

}