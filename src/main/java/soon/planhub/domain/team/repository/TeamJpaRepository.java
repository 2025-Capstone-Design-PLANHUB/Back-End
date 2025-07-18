package soon.planhub.domain.team.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import soon.planhub.domain.team.entity.Team;

public interface TeamJpaRepository extends JpaRepository<Team, Long> {

}