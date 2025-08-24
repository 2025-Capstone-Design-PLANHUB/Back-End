package soon.planhub.domain.task.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import soon.planhub.domain.task.entity.TaskLabel;
import soon.planhub.global.exception.common.EntityNotFoundException;
import soon.planhub.global.exception.dto.ErrorDetail;

@RequiredArgsConstructor
@Repository
public class TaskLabelRepository {

    private final TaskLabelJpaRepository taskLabelJpaRepository;

    public void save(TaskLabel label) {
        taskLabelJpaRepository.save(label);
    }

    public TaskLabel findById(Long id) {
        return taskLabelJpaRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException(ErrorDetail.TASK_LABEL_NOT_FOUND));
    }

    public boolean existsByTitleAndProjectId(String title, Long projectId) {
        return taskLabelJpaRepository.existsByTitleAndProjectId(title, projectId);
    }

    public void deleteById(Long id) {
        taskLabelJpaRepository.deleteById(id);
    }

}