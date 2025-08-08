package soon.planhub.domain.team.service.invitation;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import soon.planhub.global.email.EmailSender;

@RequiredArgsConstructor
@Component
public class InvitationProcessor {

    private static final String INVITATION_CODE_SUBJECT = "초대 코드 발급 안내";

    private final EmailSender emailSender;

    @Async
    public void sendInvitationEmail(String email, String invitationCode) {
        emailSender.send(email, INVITATION_CODE_SUBJECT, invitationCode);
    }

}