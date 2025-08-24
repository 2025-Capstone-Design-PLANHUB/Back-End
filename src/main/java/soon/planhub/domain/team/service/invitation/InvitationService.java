package soon.planhub.domain.team.service.invitation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import soon.planhub.domain.team.service.dto.request.InvitationSendServiceRequest;
import soon.planhub.global.annotation.TeamMembership;

@RequiredArgsConstructor
@Service
public class InvitationService {

    private final InvitationCodeGenerator codeGenerator;
    private final InvitationProcessor invitationProcessor;
    private final InvitationReader invitationReader;

    @TeamMembership
    public String generateInvitationCode(Long teamId, Long memberId) {
        return codeGenerator.generateInvitationCode(teamId);
    }

    @TeamMembership
    public void sendInvitationCode(Long teamId, Long memberId, InvitationSendServiceRequest request) {
        String code = invitationReader.findInvitationCodeByTeamId(teamId);
        request.emails()
            .forEach(email -> invitationProcessor.sendInvitationEmail(email, code));
    }

}