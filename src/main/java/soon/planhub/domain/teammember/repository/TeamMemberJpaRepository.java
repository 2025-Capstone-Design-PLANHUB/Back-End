package soon.planhub.domain.teammember.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import soon.planhub.domain.teammember.entity.Role;
import soon.planhub.domain.teammember.entity.TeamMember;

import java.util.Optional;

public interface TeamMemberJpaRepository extends JpaRepository<TeamMember, Long> {

    boolean existsByTeamIdAndRole(Long teamId, Role role);

    boolean existsByTeamIdAndMemberId(Long teamId, Long memberId);

    Optional<TeamMember> findByTeamId(Long teamId);

    @Query("SELECT tm FROM TeamMember tm JOIN FETCH tm.member WHERE tm.team.id = :teamId AND tm.role = :role")
    Optional<TeamMember> findByTeamIdAndRole(Long teamId, Role role);

}