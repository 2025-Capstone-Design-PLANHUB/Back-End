package soon.planhub.domain.team.service.invitation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import soon.planhub.domain.teammember.service.TeamMemberValidator;

import java.util.List;

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

    public void sendInvitationCode(Long teamId, Long memberId, List<String> emails) {
        teamMemberValidator.validateTeamHasMember(teamId, memberId);

        String code = invitationReader.findInvitationCodeByTeamId(teamId);
        emails.forEach(email -> invitationProcessor.sendInvitationEmail(email, code));
    }

}