package soon.planhub.domain.task.service.dto.label.request;

import lombok.Builder;
import soon.planhub.domain.task.service.dto.label.TaskLabelInformation;

@Builder
public record TaskLabelUpdateServiceRequest(

    Long teamId,
    Long projectId,
    Long labelId,
    String newTitle,
    String oldTitle,
    String description,
    String color

) {

    public TaskLabelInformation toInfo() {
        return TaskLabelInformation.builder()
            .title(newTitle)
            .description(description)
            .color(color)
            .build();
    }

}