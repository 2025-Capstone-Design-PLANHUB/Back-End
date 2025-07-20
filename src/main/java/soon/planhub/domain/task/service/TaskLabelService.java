package soon.planhub.domain.task.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import soon.planhub.domain.task.repository.TaskLabelRepository;

@RequiredArgsConstructor
@Service
public class TaskLabelService {

    private final TaskLabelRepository taskLabelRepository;

}