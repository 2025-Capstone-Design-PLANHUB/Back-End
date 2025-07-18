package soon.planhub.domain.task.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import soon.planhub.domain.task.service.TaskService;

@RequiredArgsConstructor
@RequestMapping("/api/v1/teams/{teamId}")
@RestController
public class TaskController {

    private final TaskService taskService;

}