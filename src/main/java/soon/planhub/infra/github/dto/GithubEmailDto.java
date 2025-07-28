package soon.planhub.infra.github.dto;

import lombok.Builder;

@Builder
public record GithubEmailDto(

    String email,
    boolean primary,
    boolean verified

) {

}