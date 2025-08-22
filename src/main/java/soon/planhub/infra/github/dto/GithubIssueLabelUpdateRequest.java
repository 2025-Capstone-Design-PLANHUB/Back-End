package soon.planhub.infra.github.dto;

import lombok.Builder;
import soon.planhub.domain.project.entity.Project;
import soon.planhub.domain.task.service.dto.label.request.TaskLabelUpdateServiceRequest;

@Builder
public record GithubIssueLabelUpdateRequest(

    String token,
    String organizationName,
    String repositoryName,
    String oldTitle,
    String newTitle,
    String description,
    String color

) {

    public static GithubIssueLabelUpdateRequest from(
        String token,
        Project project,
        TaskLabelUpdateServiceRequest request
    ) {
        return GithubIssueLabelUpdateRequest.builder()
            .token(token)
            .organizationName(project.getOrganizationName())
            .repositoryName(project.getTitle())
            .oldTitle(request.oldTitle())
            .newTitle(request.newTitle())
            .description(request.description())
            .color(request.color())
            .build();
    }

    public Object toBody() {
        return new GithubRequest(newTitle, description, color);
    }

    // GitHub API에 요청 할때만 사용하는 DTO
    private record GithubRequest(
        String newName,
        String description,
        String color
    ) {
    }

}