package soon.planhub.domain.task.service.label;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import soon.planhub.domain.task.repository.TaskLabelRelationRepository;
import soon.planhub.domain.task.repository.TaskLabelRepository;

@RequiredArgsConstructor
@Component
public class TaskLabelRemover {

    private final TaskLabelRepository taskLabelRepository;
    private final TaskLabelRelationRepository taskLabelRelationRepository;

    @Transactional
    public void deleteLabel(Long taskLabelId) {
        taskLabelRelationRepository.deleteAllByTaskLabelId(taskLabelId);
        taskLabelRepository.deleteById(taskLabelId);
    }

}