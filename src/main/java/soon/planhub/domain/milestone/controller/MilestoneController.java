package soon.planhub.domain.milestone.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import soon.planhub.domain.milestone.service.MilestoneService;

@RequiredArgsConstructor
@RequestMapping("/api/v1/teams/{teamId}")
@RestController
public class MilestoneController {

    private final MilestoneService milestoneService;

}