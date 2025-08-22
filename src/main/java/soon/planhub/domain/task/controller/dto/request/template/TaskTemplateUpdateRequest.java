package soon.planhub.domain.task.controller.dto.request.template;

import lombok.Builder;
import soon.planhub.domain.task.service.dto.template.request.TaskTemplateUpdateServiceRequest;

@Builder
public record TaskTemplateUpdateRequest(

    String title,
    String description,
    String content,
    String type

) {

    public TaskTemplateUpdateServiceRequest toServiceRequest(Long teamId, Long taskTemplateId) {
        return TaskTemplateUpdateServiceRequest.builder()
            .teamId(teamId)
            .taskTemplateId(taskTemplateId)
            .title(title)
            .description(description)
            .content(content)
            .type(type)
            .build();
    }

}