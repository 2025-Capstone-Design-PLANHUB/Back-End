package soon.planhub.domain.team.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import soon.planhub.domain.team.controller.dto.request.TeamCreateRequest;
import soon.planhub.domain.team.service.TeamService;
import soon.planhub.global.annotation.AuthMemberId;

@RequiredArgsConstructor
@RequestMapping("/api/v1/teams")
@RestController
public class TeamController {

    private final TeamService teamService;

    @PostMapping
    public ResponseEntity<Long> createTeam(
        @Valid @RequestBody TeamCreateRequest request,
        @AuthMemberId Long memberId
    ) {
        Long teamId = teamService.createTeam(request.toServiceRequest(), memberId);
        return ResponseEntity.status(HttpStatus.CREATED).body(teamId);
    }

}