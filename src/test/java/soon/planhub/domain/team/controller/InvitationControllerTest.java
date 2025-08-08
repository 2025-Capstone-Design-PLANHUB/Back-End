package soon.planhub.domain.team.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import soon.planhub.ControllerTestSupport;
import soon.planhub.domain.team.controller.dto.request.InvitationSendRequest;
import soon.planhub.global.annotation.TestMemberId;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class InvitationControllerTest extends ControllerTestSupport {

    private static final String BASE_URL = "/api/v1/teams/{teamId}";

    @TestMemberId
    @DisplayName("초대 코드를 생성한다.")
    @Test
    void generateInvitationCode() throws Exception {
        // given
        Long teamId = 1L;
        Long memberId = 1L;
        String expectedCode = "expected";

        given(invitationService.generateInvitationCode(teamId, memberId))
            .willReturn(expectedCode);

        // expected
        mockMvc.perform(post(BASE_URL + "/invitation-codes", teamId, memberId))
            .andExpect(status().isOk())
            .andExpect(content().string(expectedCode));
    }

    @TestMemberId
    @DisplayName("초대 이메일을 전송한다")
    @Test
    void sendInvitationEmails() throws Exception {
        // given
        Long teamId = 1L;
        InvitationSendRequest request = InvitationSendRequest.builder()
            .emails(List.of("test1@example.com", "test2@example.com"))
            .build();

        // expected
        mockMvc.perform(
                post(BASE_URL + "/invitations", teamId)
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isNoContent());
    }

}