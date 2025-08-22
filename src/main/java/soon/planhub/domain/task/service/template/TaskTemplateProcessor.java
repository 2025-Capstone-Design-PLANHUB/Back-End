package soon.planhub.domain.task.service.template;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import soon.planhub.domain.project.repository.ProjectRepository;
import soon.planhub.domain.task.entity.TaskTemplate;
import soon.planhub.domain.task.repository.TaskTemplateRepository;
import soon.planhub.domain.task.service.dto.template.TaskTemplateInformation;

@RequiredArgsConstructor
@Transactional
@Component
public class TaskTemplateProcessor {

    private final TaskTemplateRepository taskTemplateRepository;
    private final ProjectRepository projectRepository;

    public Long createTaskTemplate(TaskTemplateInformation info, Long projectId) {
        TaskTemplate taskTemplate = TaskTemplate.create(
            info.title(),
            info.description(),
            info.content(),
            info.type(),
            projectRepository.findById(projectId)
        );
        taskTemplateRepository.save(taskTemplate);
        return taskTemplate.getId();
    }

    public void updateTaskTemplate(TaskTemplateInformation info, Long taskTemplateId) {
        TaskTemplate template = taskTemplateRepository.findById(taskTemplateId);
        template.update(
            info.title(),
            info.description(),
            info.content(),
            info.type()
        );
    }

}