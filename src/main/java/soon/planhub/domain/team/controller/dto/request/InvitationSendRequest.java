package soon.planhub.domain.team.controller.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import soon.planhub.domain.team.service.dto.request.InvitationSendServiceRequest;

import java.util.List;

@Builder
public record InvitationSendRequest(

    @NotNull(message = "이메일 목록은 필수입니다.")
    @Size(min = 1, message = "최소 한 개 이상의 이메일을 입력해야 합니다.")
    List<@Email(message = "유효한 이메일 형식이어야 합니다.") String> emails

) {

    public InvitationSendServiceRequest toServiceRequest(Long teamId, Long memberId) {
        return InvitationSendServiceRequest.builder()
            .teamId(teamId)
            .memberId(memberId)
            .emails(emails)
            .build();
    }

}