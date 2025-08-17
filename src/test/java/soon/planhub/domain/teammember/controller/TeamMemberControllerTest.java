package soon.planhub.domain.teammember.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import soon.planhub.ControllerTestSupport;
import soon.planhub.domain.team.controller.dto.request.TeamMemberAppendRequest;
import soon.planhub.domain.team.service.dto.request.TeamMemberAppendServiceRequest;
import soon.planhub.global.annotation.TestMemberId;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TeamMemberControllerTest extends ControllerTestSupport {

    private static final String BASE_URL = "/api/v1/teams/{teamId}/members";

    @TestMemberId
    @DisplayName("팀원을 추가한다.")
    @Test
    void appendTeam() throws Exception {
        // given
        long teamId = 1L;
        var request = TeamMemberAppendRequest.builder()
            .invitationCode("valid-code")
            .position("BACKEND")
            .build();

        given(teamMemberService.append(any(TeamMemberAppendServiceRequest.class), anyLong()))
            .willReturn(teamId);

        // expected
        mockMvc.perform(
                post(BASE_URL, teamId)
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$").value(teamId));
    }

    @DisplayName("팀에 참여할 때 초대 코드는 필수값이다.")
    @Test
    void joinTeamWithoutInvitationCode() throws Exception {
        // given
        long teamId = 1L;
        var request = TeamMemberAppendRequest.builder()
            .position("BACKEND")
            .build();

        // expected
        mockMvc.perform(
                post(BASE_URL, teamId)
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value(400))
            .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
            .andExpect(jsonPath("$.validation.invitationCode").value("초대 코드는 비어있을 수 없습니다."));
    }

}