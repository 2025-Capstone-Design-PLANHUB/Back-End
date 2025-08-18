package soon.planhub.domain.task.service.template;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import soon.planhub.domain.task.service.dto.template.request.TaskTemplateCreateServiceRequest;
import soon.planhub.domain.teammember.service.TeamMemberValidator;

@RequiredArgsConstructor
@Service
public class TaskTemplateService {

    private final TaskTemplateProcessor taskTemplateProcessor;
    private final TeamMemberValidator teamMemberValidator;

    public Long create(TaskTemplateCreateServiceRequest request, Long memberId) {
        teamMemberValidator.validateTeamHasMember(request.teamId(), memberId);
        return taskTemplateProcessor.createTaskTemplate(request.toInfo(), request.projectId());
    }

}