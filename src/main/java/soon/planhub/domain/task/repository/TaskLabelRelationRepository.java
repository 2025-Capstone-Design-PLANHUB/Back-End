package soon.planhub.domain.task.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class TaskLabelRelationRepository {

    private final TaskLabelRelationJpaRepository taskLabelRelationJpaRepository;

}