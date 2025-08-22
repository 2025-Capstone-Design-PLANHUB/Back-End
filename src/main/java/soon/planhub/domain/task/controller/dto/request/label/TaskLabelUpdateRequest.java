package soon.planhub.domain.task.controller.dto.request.label;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import soon.planhub.domain.task.service.dto.label.request.TaskLabelUpdateServiceRequest;

@Builder
public record TaskLabelUpdateRequest(

    @Min(value = 1, message = "프로젝트 ID는 1 이상이어야 합니다.")
    @NotNull(message = "프로젝트 ID를 입력해주세요.")
    Long projectId,

    @NotBlank(message = "새로운 이름을 입력해주세요.")
    String newTitle,

    @NotBlank(message = "이전 이름을 입력해주세요.")
    String oldTitle,

    @NotBlank(message = "설명을 입력해주세요.")
    String description,

    @NotBlank(message = "색상을 입력해주세요.")
    String color

) {

    public TaskLabelUpdateServiceRequest toServiceRequest(Long teamId, Long labelId) {
        return TaskLabelUpdateServiceRequest.builder()
            .teamId(teamId)
            .projectId(projectId)
            .labelId(labelId)
            .newTitle(newTitle)
            .oldTitle(oldTitle)
            .description(description)
            .color(color)
            .build();
    }

}