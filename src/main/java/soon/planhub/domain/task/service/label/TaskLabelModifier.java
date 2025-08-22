package soon.planhub.domain.task.service.label;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import soon.planhub.domain.task.entity.TaskLabel;
import soon.planhub.domain.task.repository.TaskLabelRepository;
import soon.planhub.domain.task.service.dto.label.TaskLabelInformation;

@RequiredArgsConstructor
@Component
public class TaskLabelModifier {

    private final TaskLabelRepository taskLabelRepository;

    @Transactional
    public void updateLabel(TaskLabelInformation info, Long labelId) {
        TaskLabel label = taskLabelRepository.findById(labelId);
        label.update(info.title(), info.description(), info.color());
    }

}