package soon.planhub.domain.teammember.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import soon.planhub.domain.teammember.repository.TeamMemberRepository;
import soon.planhub.global.exception.common.EntityNotFoundException;
import soon.planhub.global.exception.dto.ErrorDetail;
import soon.planhub.global.exception.team.LeaderAlreadyExistsException;

@RequiredArgsConstructor
@Component
public class TeamMemberValidator {

    private final TeamMemberRepository teamMemberRepository;

    public void validateTeamHasNoLeader(Long teamId) {
        if (teamMemberRepository.existsByTeamIdAndRole(teamId)) {
            throw new LeaderAlreadyExistsException(ErrorDetail.TEAM_LEADER_ALREADY_EXISTS);
        }
    }

    public void validateTeamHasMember(Long teamId, Long memberId) {
        if (!teamMemberRepository.existsByTeamIdAndMemberId(teamId, memberId)) {
            throw new EntityNotFoundException(ErrorDetail.TEAM_MEMBER_NOT_FOUND);
        }
    }

}