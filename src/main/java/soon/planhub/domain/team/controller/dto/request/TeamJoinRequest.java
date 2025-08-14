package soon.planhub.domain.team.controller.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import soon.planhub.domain.team.service.dto.request.TeamJoinServiceRequest;

@Builder
public record TeamJoinRequest(

    @NotBlank(message = "초대 코드는 비어있을 수 없습니다.")
    String invitationCode,

    @NotBlank(message = "포지션은 비어있을 수 없습니다.")
    String position

) {

    public TeamJoinServiceRequest toServiceRequest(Long teamId) {
        return TeamJoinServiceRequest.builder()
            .invitationCode(invitationCode)
            .position(position)
            .teamId(teamId)
            .build();
    }

}