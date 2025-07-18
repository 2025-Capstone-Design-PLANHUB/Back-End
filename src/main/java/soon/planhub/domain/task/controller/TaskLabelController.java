package soon.planhub.domain.task.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import soon.planhub.domain.task.service.TaskLabelService;

@RequiredArgsConstructor
@RequestMapping("/api/v1/teams/{teamId}/task-labels")
@RestController
public class TaskLabelController {

    private final TaskLabelService taskLabelService;

}