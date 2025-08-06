package soon.planhub.domain.team.repository;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import soon.planhub.domain.team.entity.Team;
import soon.planhub.global.exception.common.EntityNotFoundException;
import soon.planhub.global.exception.dto.ErrorDetail;

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

    public boolean existsByInvitationCode(String code, LocalDateTime now) {
        return teamJpaRepository.existsByInvitationCode_CodeAndInvitationCode_ExpirationTimeAfter(
            code, now
        );
    }

    public void deleteAllInBatch() {
        teamJpaRepository.deleteAllInBatch();
    }

}