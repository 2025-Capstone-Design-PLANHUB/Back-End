package soon.planhub.infra.github.dto;

import lombok.Builder;

@Builder
public record GithubIssueLabelGetRequest(

    String token,
    String organizationName,
    String repositoryName

) {
}