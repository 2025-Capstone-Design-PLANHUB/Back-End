package soon.planhub.domain.teammember.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import soon.planhub.domain.teammember.entity.Role;
import soon.planhub.domain.teammember.entity.TeamMember;
import soon.planhub.global.exception.common.EntityNotFoundException;
import soon.planhub.global.exception.dto.ErrorDetail;

@RequiredArgsConstructor
@Repository
public class TeamMemberRepository {

    private final TeamMemberJpaRepository teamMemberJpaRepository;

    public void save(TeamMember teamMember) {
        teamMemberJpaRepository.save(teamMember);
    }

    public TeamMember findByTeamId(Long teamId) {
        return teamMemberJpaRepository.findByTeamId(teamId)
            .orElseThrow(() -> new EntityNotFoundException(ErrorDetail.TEAM_MEMBER_NOT_FOUND));
    }

    public boolean existsByTeamIdAndRole(Long teamId) {
        return teamMemberJpaRepository.existsByTeamIdAndRole(teamId, Role.ROLE_LEADER);
    }

    public void deleteAllInBatch() {
        teamMemberJpaRepository.deleteAllInBatch();
    }

}