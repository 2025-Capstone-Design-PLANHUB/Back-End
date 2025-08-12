package soon.planhub.domain.team.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import soon.planhub.domain.team.service.dto.request.TeamCreateServiceRequest;
import soon.planhub.domain.team.service.dto.request.TeamJoinServiceRequest;
import soon.planhub.domain.teammember.service.TeamMemberAppender;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class TeamService {

    private final TeamCreator teamCreator;
    private final TeamMemberAppender teamMemberAppender;
    private final TeamValidator teamValidator;

    public Long createTeam(TeamCreateServiceRequest request, Long creatorId) {
        teamValidator.validateAdminPermission(request.organizationName(), creatorId);
        return teamCreator.createTeam(request.toInfo(), creatorId);
    }

    public Long joinTeam(TeamJoinServiceRequest request, Long memberId) {
        LocalDateTime now = LocalDateTime.now();
        teamValidator.validateInvitationCode(request.invitationCode(), now);

        return teamMemberAppender.appendToMember(memberId, request.teamId(), request.position());
    }

}