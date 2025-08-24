package soon.planhub.infra.github.dto;

import lombok.Builder;
import soon.planhub.domain.project.entity.Project;

@Builder
public record GithubIssueLabelGetRequest(

    String token,
    String organizationName,
    String repositoryName

) {

    public static GithubIssueLabelGetRequest from(String token, Project project) {
        return GithubIssueLabelGetRequest.builder()
            .token(token)
            .organizationName(project.getOrganizationName())
            .repositoryName(project.getTitle())
            .build();
    }

}