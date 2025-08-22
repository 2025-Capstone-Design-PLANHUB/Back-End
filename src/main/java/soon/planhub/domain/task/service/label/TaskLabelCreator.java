package soon.planhub.domain.task.service.label;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import soon.planhub.domain.project.entity.Project;
import soon.planhub.domain.project.repository.ProjectRepository;
import soon.planhub.domain.task.entity.TaskLabel;
import soon.planhub.domain.task.repository.TaskLabelRepository;
import soon.planhub.domain.task.service.dto.label.TaskLabelInformation;

@RequiredArgsConstructor
@Component
public class TaskLabelCreator {

    private final TaskLabelRepository taskLabelRepository;
    private final ProjectRepository projectRepository;

    public Long createLabel(TaskLabelInformation info, Long projectId) {
        Project project = projectRepository.findById(projectId);
        TaskLabel label = TaskLabel.create(info.title(), info.description(), info.color(), project);
        taskLabelRepository.save(label);

        return label.getId();
    }

}