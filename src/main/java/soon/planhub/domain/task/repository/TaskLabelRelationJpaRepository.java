package soon.planhub.domain.task.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import soon.planhub.domain.task.entity.TaskLabelRelation;

public interface TaskLabelRelationJpaRepository extends JpaRepository<TaskLabelRelation, Long> {

}