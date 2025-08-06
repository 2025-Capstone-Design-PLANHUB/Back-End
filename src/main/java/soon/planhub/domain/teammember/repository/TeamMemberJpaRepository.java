package soon.planhub.domain.teammember.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import soon.planhub.domain.teammember.entity.Role;
import soon.planhub.domain.teammember.entity.TeamMember;

import java.util.Optional;

public interface TeamMemberJpaRepository extends JpaRepository<TeamMember, Long> {

    boolean existsByTeamIdAndRole(Long teamId, Role role);

    boolean existsByTeamIdAndMemberId(Long teamId, Long memberId);

    Optional<TeamMember> findByTeamId(Long teamId);

}