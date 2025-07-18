package soon.planhub.domain.task.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import soon.planhub.domain.task.repository.TaskLabelRelationRepository;

@RequiredArgsConstructor
@Service
public class TaskLabelRelationService {

    private final TaskLabelRelationRepository taskLabelRelationRepository;

}