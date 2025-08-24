package soon.planhub.domain.teammember.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import soon.planhub.domain.team.service.TeamValidator;
import soon.planhub.domain.teammember.port.out.TeamMemberPort;
import soon.planhub.domain.teammember.service.dto.request.TeamMemberAppendServiceRequest;
import soon.planhub.domain.teammember.service.dto.request.TeamMemberPositionModifyServiceRequest;
import soon.planhub.domain.teammember.service.dto.response.TeamMemberDetailResponse;
import soon.planhub.global.annotation.TeamMembership;

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

    public Long append(Long teamId, Long memberId, TeamMemberAppendServiceRequest request) {
        LocalDateTime now = LocalDateTime.now();
        teamValidator.validateInvitationCode(request.invitationCode(), now); // TODO: TeamValidator가 여기에 존재하는 것이 맞는지 검토 필요

        teamMemberPort.appendMemberToOrg(teamId, memberId); // 깃 허브에 요청
        return teamMemberAppender.appendToMember(memberId, teamId, request.position());
    }

    @TeamMembership
    public List<TeamMemberDetailResponse> getTeamMembers(Long teamId, Long memberId) {
        return teamMemberReader.getTeamMembers(teamId);
    }

    @TeamMembership
    public void updatePosition(Long teamId, Long memberId, TeamMemberPositionModifyServiceRequest request) {
        teamMemberModifier.updatePosition(request.teamMemberId(), request.position());
    }

    @TeamMembership
    public void updateVisibility(Long teamId, Long memberId, boolean visible) {
        teamMemberModifier.updateVisibility(teamId, memberId, visible);
    }

}