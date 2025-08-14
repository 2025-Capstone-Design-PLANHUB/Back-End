package soon.planhub.domain.team.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import soon.planhub.domain.team.controller.dto.request.TeamCreateRequest;
import soon.planhub.domain.team.controller.dto.request.TeamJoinRequest;
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
        return ResponseEntity.ok(teamId);
    }

    @PostMapping("/{teamId}/members")
    public ResponseEntity<Long> joinTeam(
        @Valid @RequestBody TeamJoinRequest request,
        @AuthMemberId Long memberId,
        @PathVariable Long teamId
    ) {
        Long joinedTeamId = teamService.joinTeam(request.toServiceRequest(teamId), memberId);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(joinedTeamId);
    }

}