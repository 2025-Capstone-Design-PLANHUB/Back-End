package soon.planhub.domain.task.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import soon.planhub.domain.task.entity.Task;

public interface TaskJpaRepository extends JpaRepository<Task, Long> {

}