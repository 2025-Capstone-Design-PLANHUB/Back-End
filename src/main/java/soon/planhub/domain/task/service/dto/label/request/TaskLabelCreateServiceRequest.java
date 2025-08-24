package soon.planhub.domain.task.service.dto.label.request;

import lombok.Builder;
import soon.planhub.domain.task.service.dto.label.TaskLabelInformation;

@Builder
public record TaskLabelCreateServiceRequest(

    Long projectId,
    String title,
    String description,
    String color

) {

    public TaskLabelInformation toInfo() {
        return TaskLabelInformation.builder()
            .title(title)
            .description(description)
            .color(color)
            .build();
    }

}