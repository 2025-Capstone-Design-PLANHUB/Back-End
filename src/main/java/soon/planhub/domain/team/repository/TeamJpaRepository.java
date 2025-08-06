package soon.planhub.domain.team.repository;

import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import soon.planhub.domain.team.entity.Team;

public interface TeamJpaRepository extends JpaRepository<Team, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints(@QueryHint(name = "jakarta.persistence.lock.timeout", value = "2000")) // 2초
    @Query("select t from Team t where t.id = :id")
    Optional<Team> findByIdWithPessimisticLock(Long id);

    boolean existsByInvitationCode_CodeAndInvitationCode_ExpirationTimeAfter(
        String code, LocalDateTime now
    );

}