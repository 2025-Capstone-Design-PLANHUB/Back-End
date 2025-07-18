package soon.planhub.domain.teammember.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import soon.planhub.domain.teammember.service.TeamMemberService;

@RequiredArgsConstructor
@RequestMapping("/api/v1/teams/{teamId}/members")
@RestController
public class TeamMemberController {

    private final TeamMemberService teamMemberService;

}