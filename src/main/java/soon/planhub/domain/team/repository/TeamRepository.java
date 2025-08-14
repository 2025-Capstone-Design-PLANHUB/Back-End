package soon.planhub.domain.team.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import soon.planhub.domain.team.entity.Team;
import soon.planhub.global.exception.common.EntityNotFoundException;
import soon.planhub.global.exception.dto.ErrorDetail;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Repository
public class TeamRepository {

    private final TeamJpaRepository teamJpaRepository;

    public void save(Team team) {
        teamJpaRepository.save(team);
    }

    public Team findById(Long teamId) {
        return teamJpaRepository.findById(teamId)
            .orElseThrow(() -> new EntityNotFoundException(ErrorDetail.TEAM_NOT_FOUND));
    }

    public Team findByIdWithPessimisticLock(Long teamId) {
        return teamJpaRepository.findByIdWithPessimisticLock(teamId)
            .orElseThrow(() -> new EntityNotFoundException(ErrorDetail.TEAM_NOT_FOUND));
    }

    public String findInvitationCodeByTeamId(Long teamId) {
        return teamJpaRepository.findInvitationCodeById(teamId)
            .orElseThrow(() -> new EntityNotFoundException(ErrorDetail.TEAM_NOT_FOUND));
    }

    public Team findTeamByValidInvitationCode(String code, LocalDateTime now) {
        return teamJpaRepository.findTeamByValidInvitationCode(code, now)
            .orElseThrow(() -> new EntityNotFoundException(ErrorDetail.INVALID_INVITATION_CODE));
    }

    public boolean existsByInvitationCode(String code, LocalDateTime now) {
        return teamJpaRepository.existsByInvitationCode_CodeAndInvitationCode_ExpirationTimeAfter(
            code, now
        );
    }

    public void deleteAllInBatch() {
        teamJpaRepository.deleteAllInBatch();
    }

}