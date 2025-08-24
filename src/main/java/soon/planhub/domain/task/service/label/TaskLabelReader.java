package soon.planhub.domain.task.service.label;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import soon.planhub.domain.task.entity.TaskLabel;
import soon.planhub.domain.task.repository.TaskLabelRepository;

@RequiredArgsConstructor
@Component
public class TaskLabelReader {

    private final TaskLabelRepository taskLabelRepository;

    public TaskLabel getLabelById(Long labelId) {
        return taskLabelRepository.findById(labelId);

    }

}