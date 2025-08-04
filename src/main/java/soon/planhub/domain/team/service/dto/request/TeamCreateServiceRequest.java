package soon.planhub.domain.team.service.dto.request;

import lombok.Builder;
import soon.planhub.domain.team.service.dto.TeamInformation;

@Builder
public record TeamCreateServiceRequest(

    String name,
    String description,
    String organizationName

) {

    public TeamInformation toInfo() {
        return TeamInformation.builder()
            .name(name)
            .description(description)
            .organizationName(organizationName)
            .build();
    }

}