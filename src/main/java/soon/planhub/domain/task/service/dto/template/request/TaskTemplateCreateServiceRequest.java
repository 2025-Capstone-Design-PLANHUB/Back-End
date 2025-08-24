package soon.planhub.domain.task.service.dto.template.request;

import lombok.Builder;
import soon.planhub.domain.task.service.dto.template.TaskTemplateInformation;

@Builder
public record TaskTemplateCreateServiceRequest(

    String title,
    String description,
    String content,
    String type,
    Long projectId

) {

    public TaskTemplateInformation toInfo() {
        return TaskTemplateInformation.builder()
            .title(title)
            .description(description)
            .content(content)
            .type(type)
            .build();
    }

}