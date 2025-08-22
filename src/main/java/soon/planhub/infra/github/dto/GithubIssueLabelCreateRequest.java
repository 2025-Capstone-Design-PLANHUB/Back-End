package soon.planhub.infra.github.dto;

import lombok.Builder;
import soon.planhub.domain.project.entity.Project;
import soon.planhub.domain.task.service.dto.label.TaskLabelInformation;

@Builder
public record GithubIssueLabelCreateRequest(

    String token,
    String name,
    String color,
    String description,
    String organizationName,
    String repositoryName

) {

    public static GithubIssueLabelCreateRequest from(String token, TaskLabelInformation info, Project project) {
        return GithubIssueLabelCreateRequest.builder()
            .token(token)
            .name(info.title())
            .color(info.color())
            .description(info.description())
            .organizationName(project.getOrganizationName())
            .repositoryName(project.getTitle())
            .build();
    }

    public Object toBody() {
        return new GithubRequest(name, color, description);
    }

    // GitHub API에 요청 할때만 사용하는 DTO
    private record GithubRequest(
        String name,
        String color,
        String description
    ) {
    }

}