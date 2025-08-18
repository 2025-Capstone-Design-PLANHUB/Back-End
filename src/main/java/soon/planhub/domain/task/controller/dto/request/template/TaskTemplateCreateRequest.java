package soon.planhub.domain.task.controller.dto.request.template;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import soon.planhub.domain.task.service.dto.template.request.TaskTemplateCreateServiceRequest;

@Builder
public record TaskTemplateCreateRequest(

    @NotBlank(message = "제목은 비어있을 수 없습니다.")
    String title,

    @NotBlank(message = "설명은 비어있을 수 없습니다.")
    String description,

    @NotBlank(message = "내용은 비어있을 수 없습니다.")
    String content,

    @NotBlank(message = "타입은 비어있을 수 없습니다.")
    String type,

    @Min(value = 1, message = "프로젝트 ID는 1 이상이어야 합니다.")
    @NotNull(message = "프로젝트 ID는 비어있을 수 없습니다.")
    Long projectId

) {

    public TaskTemplateCreateServiceRequest toServiceRequest(Long teamId) {
        return TaskTemplateCreateServiceRequest.builder()
            .title(title)
            .description(description)
            .content(content)
            .type(type)
            .projectId(projectId)
            .teamId(teamId)
            .build();
    }

}