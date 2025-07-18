package soon.planhub.domain.task.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class TaskTemplateRepository {

    private final TaskTemplateJpaRepository taskTemplateJpaRepository;

}