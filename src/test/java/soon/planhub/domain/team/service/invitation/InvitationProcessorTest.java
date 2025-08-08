package soon.planhub.domain.team.service.invitation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import soon.planhub.IntegrationTestSupport;
import soon.planhub.global.email.EmailSender;

import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.verify;

class InvitationProcessorTest extends IntegrationTestSupport {

    @Autowired
    private InvitationProcessor invitationProcessor;

    @MockitoBean
    private EmailSender emailSender;

    @DisplayName("초대 코드를 포함한 이메일을 비동기적으로 발송한다.")
    @Test
    void sendInvitationEmail() {
        // given
        String email = "test@example.com";
        String invitationCode = "TESTCODE";
        String expectedSubject = "초대 코드 발급 안내";

        // when
        invitationProcessor.sendInvitationEmail(email, invitationCode);

        // then
        await().atMost(2, TimeUnit.SECONDS)
            .untilAsserted(() ->
                verify(emailSender).send(email, expectedSubject, invitationCode)
            );
    }

}