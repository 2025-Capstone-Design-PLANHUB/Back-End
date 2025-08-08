package soon.planhub.domain.team.service.invitation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import soon.planhub.domain.team.service.dto.request.InvitationSendServiceRequest;
import soon.planhub.domain.teammember.service.TeamMemberValidator;

@RequiredArgsConstructor
@Service
public class InvitationService {

    private final InvitationCodeGenerator codeGenerator;
    private final InvitationProcessor invitationProcessor;
    private final InvitationReader invitationReader;
    private final TeamMemberValidator teamMemberValidator;

    public String generateInvitationCode(Long teamId, Long memberId) {
        teamMemberValidator.validateTeamHasMember(teamId, memberId);
        return codeGenerator.generateInvitationCode(teamId);
    }

    public void sendInvitationCode(InvitationSendServiceRequest request) {
        teamMemberValidator.validateTeamHasMember(request.teamId(), request.memberId());

        String code = invitationReader.findInvitationCodeByTeamId(request.teamId());
        request.emails()
            .forEach(email -> invitationProcessor.sendInvitationEmail(email, code));
    }

}