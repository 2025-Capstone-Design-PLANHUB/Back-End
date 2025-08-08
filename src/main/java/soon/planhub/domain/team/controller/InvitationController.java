package soon.planhub.domain.team.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import soon.planhub.domain.team.controller.dto.request.InvitationSendRequest;
import soon.planhub.domain.team.service.invitation.InvitationService;
import soon.planhub.global.annotation.AuthMemberId;

@RequiredArgsConstructor
@RequestMapping("/api/v1/teams/{teamId}")
@RestController
public class InvitationController {

    private final InvitationService invitationService;

    @PostMapping("/invitation-codes")
    public ResponseEntity<String> generateInvitationCode(
        @AuthMemberId Long memberId,
        @PathVariable Long teamId
    ) {
        String invitationCode = invitationService.generateInvitationCode(teamId, memberId);

        return ResponseEntity.ok(invitationCode);
    }

    @PostMapping("/invitations")
    public ResponseEntity<Void> sendInvitationCode(
        @Valid @RequestBody InvitationSendRequest request,
        @AuthMemberId Long memberId,
        @PathVariable Long teamId
    ) {
        invitationService.sendInvitationCode(request.toServiceRequest(teamId, memberId));

        return ResponseEntity.noContent().build();
    }

}