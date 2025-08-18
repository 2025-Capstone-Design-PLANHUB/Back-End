package soon.planhub.domain.teammember.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import soon.planhub.domain.team.service.TeamValidator;
import soon.planhub.domain.teammember.port.out.TeamMemberPort;
import soon.planhub.domain.teammember.service.dto.request.TeamMemberAppendServiceRequest;
import soon.planhub.domain.teammember.service.dto.request.TeamMemberPositionModifyServiceRequest;
import soon.planhub.domain.teammember.service.dto.response.TeamMemberDetailResponse;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class TeamMemberService {

    private final TeamMemberAppender teamMemberAppender;
    private final TeamMemberReader teamMemberReader;
    private final TeamMemberModifier teamMemberModifier;
    private final TeamMemberPort teamMemberPort;
    private final TeamValidator teamValidator;
    private final TeamMemberValidator teamMemberValidator;

    public Long append(TeamMemberAppendServiceRequest request, Long memberId) {
        LocalDateTime now = LocalDateTime.now();
        teamValidator.validateInvitationCode(request.invitationCode(), now); // TODO: TeamValidator가 여기에 존재하는 것이 맞는지 검토 필요

        teamMemberPort.appendMemberToOrg(request.teamId(), memberId); // 깃 허브에 요청
        return teamMemberAppender.appendToMember(memberId, request.teamId(), request.position());
    }

    public List<TeamMemberDetailResponse> getTeamMembers(Long teamId, Long memberId) {
        teamMemberValidator.validateTeamHasMember(teamId, memberId);
        return teamMemberReader.getTeamMembers(teamId);
    }

    public void updatePosition(TeamMemberPositionModifyServiceRequest request, Long memberId) {
        teamMemberValidator.validateTeamHasMember(request.teamId(), memberId);
        teamMemberModifier.updatePosition(request.teamMemberId(), request.position());
    }

}