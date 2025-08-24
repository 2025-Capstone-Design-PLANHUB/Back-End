package soon.planhub.domain.task.service.dto.template.request;

import lombok.Builder;
import soon.planhub.domain.task.service.dto.template.TaskTemplateInformation;

@Builder
public record TaskTemplateUpdateServiceRequest(

    Long taskTemplateId,
    String title,
    String description,
    String content,
    String type

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