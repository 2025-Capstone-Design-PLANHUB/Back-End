package soon.planhub.domain.team.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import soon.planhub.domain.team.service.invitation.InvitationService;
import soon.planhub.global.annotation.AuthMemberId;

@RequiredArgsConstructor
@RequestMapping("/api/v1/teams/{teamId}/invitation-codes")
@RestController
public class InvitationController {

    private final InvitationService invitationService;

    @PostMapping
    public ResponseEntity<String> generateInvitationCode(
        @AuthMemberId Long memberId,
        @PathVariable Long teamId
    ) {
        String invitationCode = invitationService.generateInvitationCode(teamId, memberId);

        return ResponseEntity.ok(invitationCode);
    }

}