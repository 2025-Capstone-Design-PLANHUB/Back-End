package soon.planhub.domain.task.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import soon.planhub.domain.task.entity.TaskLabel;

import java.util.List;

public interface TaskLabelJpaRepository extends JpaRepository<TaskLabel, Long> {

    boolean existsByTitleAndProjectId(String title, Long projectId);

    List<TaskLabel> findAllByProjectId(Long projectId);

}