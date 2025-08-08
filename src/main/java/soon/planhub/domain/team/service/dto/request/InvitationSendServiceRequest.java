package soon.planhub.domain.team.service.dto.request;

import lombok.Builder;

import java.util.List;

@Builder
public record InvitationSendServiceRequest(

    Long teamId,
    Long memberId,
    List<String> emails

) {
}