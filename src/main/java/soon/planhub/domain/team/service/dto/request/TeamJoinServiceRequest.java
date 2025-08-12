package soon.planhub.domain.team.service.dto.request;

import lombok.Builder;

@Builder
public record TeamJoinServiceRequest(

    String invitationCode,
    String position,
    Long teamId

) {

}