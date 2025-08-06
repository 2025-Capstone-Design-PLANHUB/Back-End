package soon.planhub.domain.team.service.invitation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import soon.planhub.domain.teammember.service.TeamMemberValidator;

@RequiredArgsConstructor
@Service
public class InvitationService {

    private final InvitationCodeGenerator codeGenerator;
    private final TeamMemberValidator teamMemberValidator;

    public String generateInvitationCode(Long teamId, Long memberId) {
        teamMemberValidator.validateTeamHasMember(teamId, memberId);
        return codeGenerator.generateInvitationCode(teamId);
    }

}