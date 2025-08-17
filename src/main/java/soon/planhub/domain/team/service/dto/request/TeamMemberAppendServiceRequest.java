package soon.planhub.domain.team.service.dto.request;

import lombok.Builder;

@Builder
public record TeamMemberAppendServiceRequest(

    String invitationCode,
    String position,
    Long teamId

) {

}