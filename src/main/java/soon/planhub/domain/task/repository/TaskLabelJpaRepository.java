package soon.planhub.domain.task.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import soon.planhub.domain.task.entity.TaskLabel;

public interface TaskLabelJpaRepository extends JpaRepository<TaskLabel, Long> {

}