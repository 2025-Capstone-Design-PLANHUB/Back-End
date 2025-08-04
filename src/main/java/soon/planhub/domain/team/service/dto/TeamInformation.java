package soon.planhub.domain.team.service.dto;

import lombok.Builder;

@Builder
public record TeamInformation(

    String name,
    String description,
    String organizationName

) {

}