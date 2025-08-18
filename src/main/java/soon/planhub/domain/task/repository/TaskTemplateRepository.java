package soon.planhub.domain.task.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import soon.planhub.domain.task.entity.TaskTemplate;
import soon.planhub.global.exception.common.EntityNotFoundException;
import soon.planhub.global.exception.dto.ErrorDetail;

@RequiredArgsConstructor
@Repository
public class TaskTemplateRepository {

    private final TaskTemplateJpaRepository taskTemplateJpaRepository;

    public void save(TaskTemplate taskTemplate) {
        taskTemplateJpaRepository.save(taskTemplate);
    }

    public TaskTemplate findById(Long taskTemplateId) {
        return taskTemplateJpaRepository.findById(taskTemplateId)
            .orElseThrow(() -> new EntityNotFoundException(ErrorDetail.TASK_TEMPLATE_NOT_FOUND));
    }

}