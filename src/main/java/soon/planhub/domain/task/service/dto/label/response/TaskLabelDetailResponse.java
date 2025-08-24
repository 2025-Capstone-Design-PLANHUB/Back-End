package soon.planhub.domain.task.service.dto.label.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import soon.planhub.domain.task.entity.TaskLabel;
import soon.planhub.infra.github.dto.GithubIssueLabelDetailResponse;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class TaskLabelDetailResponse {

    private Long labelId;
    private String name;
    private String color;
    private String description;

    @Builder(toBuilder = true)
    private TaskLabelDetailResponse(Long labelId, String name, String color, String description) {
        this.labelId = labelId;
        this.name = name;
        this.color = color;
        this.description = description;
    }

    public TaskLabelDetailResponse withLabelId(Long newId) {
        return toBuilder()
            .labelId(newId)
            .build();
    }

    public static TaskLabelDetailResponse of(TaskLabel taskLabel) {
        return TaskLabelDetailResponse.builder()
            .labelId(taskLabel.getId())
            .name(taskLabel.getTitle())
            .color(taskLabel.getColor())
            .description(taskLabel.getDescription())
            .build();
    }

    public static TaskLabelDetailResponse of(GithubIssueLabelDetailResponse response) {
        return TaskLabelDetailResponse.builder()
            .name(response.name())
            .color(response.color())
            .description(response.description())
            .build();
    }

}