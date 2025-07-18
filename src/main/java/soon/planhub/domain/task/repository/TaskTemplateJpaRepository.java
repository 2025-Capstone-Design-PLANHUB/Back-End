package soon.planhub.domain.task.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import soon.planhub.domain.task.entity.TaskTemplate;

public interface TaskTemplateJpaRepository extends JpaRepository<TaskTemplate, Long> {

}