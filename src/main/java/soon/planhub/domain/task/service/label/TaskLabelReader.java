package soon.planhub.domain.task.service.label;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import soon.planhub.domain.task.entity.TaskLabel;
import soon.planhub.domain.task.repository.TaskLabelRepository;
import soon.planhub.domain.task.service.dto.label.response.TaskLabelDetailResponse;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class TaskLabelReader {

    private final TaskLabelRepository taskLabelRepository;

    public TaskLabel getLabelById(Long labelId) {
        return taskLabelRepository.findById(labelId);

    }

    @Transactional(readOnly = true)
    public List<TaskLabelDetailResponse> readLabels(Long projectId, List<TaskLabelDetailResponse> labels) {
        Map<String, TaskLabel> labelMap = taskLabelRepository.findAllByProjectId(projectId)
            .stream()
            .collect(Collectors.toMap(TaskLabel::getTitle, Function.identity()));

        return labels.stream()
            .map(response -> {
                TaskLabel label = labelMap.get(response.getName());
                return (label != null) ? response.withLabelId(label.getId()) : null;
            })
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }

}