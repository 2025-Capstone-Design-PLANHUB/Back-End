package soon.planhub.domain.task.service.label;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import soon.planhub.domain.task.repository.TaskLabelRepository;
import soon.planhub.global.exception.common.InvalidRequest;
import soon.planhub.global.exception.dto.ErrorDetail;

@RequiredArgsConstructor
@Component
public class TaskLabelValidator {

    private final TaskLabelRepository taskLabelRepository;

    public void validateLabelNotExists(String title, Long projectId) {
        boolean isAlready = taskLabelRepository.existsByTitleAndProjectId(title, projectId);
        if (isAlready) {
            throw new InvalidRequest("label", ErrorDetail.TASK_LABEL_ALREADY_EXISTS.getMessage());
        }
    }

}