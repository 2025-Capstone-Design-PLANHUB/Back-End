package soon.planhub.domain.team.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import soon.planhub.ControllerTestSupport;
import soon.planhub.global.annotation.TestMemberId;

class InvitationControllerTest extends ControllerTestSupport {

    private static final String BASE_URL = "/api/v1/teams/{teamId}/invitation-codes";

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
        mockMvc.perform(post(BASE_URL, teamId, memberId))
            .andExpect(status().isOk())
            .andExpect(content().string(expectedCode));
    }

}