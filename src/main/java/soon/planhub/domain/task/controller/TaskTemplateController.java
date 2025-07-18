package soon.planhub.domain.task.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import soon.planhub.domain.task.service.TaskTemplateService;

@RequiredArgsConstructor
@RequestMapping("/api/v1/teams/{teamId}/task-templates")
@RestController
public class TaskTemplateController {

    private final TaskTemplateService taskTemplateService;

}