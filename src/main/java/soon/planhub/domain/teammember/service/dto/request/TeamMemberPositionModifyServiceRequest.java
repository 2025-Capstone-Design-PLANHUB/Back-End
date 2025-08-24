package soon.planhub.domain.teammember.service.dto.request;

import lombok.Builder;

@Builder
public record TeamMemberPositionModifyServiceRequest(

    Long teamMemberId,
    String position

) {

}