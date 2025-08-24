package soon.planhub.infra.github.dto;

import lombok.Builder;
import soon.planhub.domain.project.entity.Project;

@Builder
public record GithubIssueLabelDeleteRequest(

    String token,
    String organizationName,
    String repositoryName,
    String title

) {

    public static GithubIssueLabelDeleteRequest from(String token, Project project, String title) {
        return GithubIssueLabelDeleteRequest.builder()
            .token(token)
            .organizationName(project.getOrganizationName())
            .repositoryName(project.getTitle())
            .title(title)
            .build();
    }

}