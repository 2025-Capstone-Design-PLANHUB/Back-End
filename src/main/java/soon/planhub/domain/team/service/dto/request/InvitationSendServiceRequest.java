package soon.planhub.domain.team.service.dto.request;

import lombok.Builder;

import java.util.List;

@Builder
public record InvitationSendServiceRequest(

    List<String> emails

) {
}