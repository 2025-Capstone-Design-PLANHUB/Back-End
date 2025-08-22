package soon.planhub.domain.task.service.dto.label;

import lombok.Builder;

@Builder
public record TaskLabelInformation(

    String title,
    String description,
    String color

) {
}