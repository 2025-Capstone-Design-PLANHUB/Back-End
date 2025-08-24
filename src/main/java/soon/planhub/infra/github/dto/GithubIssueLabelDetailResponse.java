package soon.planhub.infra.github.dto;

public record GithubIssueLabelDetailResponse(

    String name,
    String color,
    String description

) {
}