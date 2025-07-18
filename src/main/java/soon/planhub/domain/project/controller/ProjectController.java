package soon.planhub.domain.project.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import soon.planhub.domain.project.service.ProjectService;

@RequiredArgsConstructor
@RequestMapping("/api/v1/{teamId}/projects")
@RestController
public class ProjectController {

    private final ProjectService projectService;

}