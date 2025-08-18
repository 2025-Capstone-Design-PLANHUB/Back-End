package soon.planhub.domain.task.service.dto.template;

import lombok.Builder;

@Builder
public record TaskTemplateInformation(

    String title,
    String description,
    String content,
    String type

) {
}