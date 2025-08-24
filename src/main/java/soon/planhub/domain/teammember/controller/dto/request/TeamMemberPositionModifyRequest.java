package soon.planhub.domain.teammember.controller.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import soon.planhub.domain.teammember.service.dto.request.TeamMemberPositionModifyServiceRequest;

@Builder
public record TeamMemberPositionModifyRequest(

    @Min(value = 1, message = "팀 ID는 1 이상의 값이어야 합니다.")
    @NotNull(message = "팀 ID는 비어있을 수 없습니다.")
    Long teamMemberId,

    @NotBlank(message = "포지션은 비어있을 수 없습니다.")
    String position

) {

    public TeamMemberPositionModifyServiceRequest toServiceRequest() {
        return TeamMemberPositionModifyServiceRequest.builder()
            .teamMemberId(teamMemberId)
            .position(position)
            .build();
    }

}