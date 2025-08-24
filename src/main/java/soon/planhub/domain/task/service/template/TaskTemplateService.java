package soon.planhub.domain.task.service.template;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import soon.planhub.domain.task.service.dto.template.request.TaskTemplateCreateServiceRequest;
import soon.planhub.domain.task.service.dto.template.request.TaskTemplateUpdateServiceRequest;
import soon.planhub.global.annotation.TeamMembership;

@RequiredArgsConstructor
@Service
public class TaskTemplateService {

    private final TaskTemplateProcessor taskTemplateProcessor;

    @TeamMembership
    public Long create(Long teamId, Long memberId, TaskTemplateCreateServiceRequest request) {
        return taskTemplateProcessor.createTaskTemplate(request.toInfo(), request.projectId());
    }

    @TeamMembership
    public void update(Long teamId, Long memberId, TaskTemplateUpdateServiceRequest request) {
        taskTemplateProcessor.updateTaskTemplate(request.toInfo(), request.taskTemplateId());
    }

}