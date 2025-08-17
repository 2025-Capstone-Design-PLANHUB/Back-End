package soon.planhub.domain.teammember.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import soon.planhub.domain.teammember.controller.dto.request.TeamMemberAppendRequest;
import soon.planhub.domain.teammember.service.TeamMemberService;
import soon.planhub.domain.teammember.service.dto.response.TeamMemberDetailResponse;
import soon.planhub.global.annotation.AuthMemberId;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/v1/teams/{teamId}/members")
@RestController
public class TeamMemberController {

    private final TeamMemberService teamMemberService;

    @PostMapping
    public ResponseEntity<Long> joinTeam(
        @Valid @RequestBody TeamMemberAppendRequest request,
        @AuthMemberId Long memberId,
        @PathVariable Long teamId
    ) {
        Long joinedTeamId = teamMemberService.append(request.toServiceRequest(teamId), memberId);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(joinedTeamId);
    }

    @GetMapping
    public ResponseEntity<List<TeamMemberDetailResponse>> getTeamMembers(
        @AuthMemberId Long memberId,
        @PathVariable Long teamId
    ) {
        List<TeamMemberDetailResponse> response = teamMemberService.getTeamMembers(teamId, memberId);
        return ResponseEntity.ok(response);
    }

}