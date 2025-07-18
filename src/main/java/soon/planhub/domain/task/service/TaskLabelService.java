package soon.planhub.domain.task.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TaskLabelService {

    private final TaskLabelRepository taskLabelRepository;

}