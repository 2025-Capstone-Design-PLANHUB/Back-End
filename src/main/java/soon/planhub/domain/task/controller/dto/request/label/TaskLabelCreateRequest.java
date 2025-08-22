package soon.planhub.domain.task.controller.dto.request.label;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import soon.planhub.domain.task.service.dto.label.request.TaskLabelCreateServiceRequest;

@Builder
public record TaskLabelCreateRequest(

    @Min(value = 1, message = "프로젝트 ID는 1 이상이어야 합니다.")
    @NotNull(message = "프로젝트 ID를 입력해주세요.")
    Long projectId,

    @NotBlank(message = "제목을 입력해주세요.")
    String title,

    @NotBlank(message = "설명을 입력해주세요.")
    String description,

    @NotBlank(message = "색상을 입력해주세요.")
    String color

) {

    public TaskLabelCreateServiceRequest toServiceRequest(Long teamId) {
        return TaskLabelCreateServiceRequest.builder()
            .teamId(teamId)
            .projectId(projectId)
            .title(title)
            .description(description)
            .color(color)
            .build();
    }

}