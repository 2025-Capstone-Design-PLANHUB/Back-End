package soon.planhub.domain.milestone.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import soon.planhub.domain.milestone.entity.Milestone;

public interface MilestoneJpaRepository extends JpaRepository<Milestone, Long> {

}