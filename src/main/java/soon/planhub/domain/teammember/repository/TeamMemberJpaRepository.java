package soon.planhub.domain.teammember.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import soon.planhub.domain.teammember.entity.TeamMember;

public interface TeamMemberJpaRepository extends JpaRepository<TeamMember, Long> {

}