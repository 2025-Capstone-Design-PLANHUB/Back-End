package soon.planhub.domain.teammember.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import soon.planhub.domain.teammember.entity.Role;
import soon.planhub.domain.teammember.entity.TeamMember;

public interface TeamMemberJpaRepository extends JpaRepository<TeamMember, Long> {

    boolean existsByTeamIdAndRole(Long teamId, Role role);

    Optional<TeamMember> findByTeamId(Long teamId);

}