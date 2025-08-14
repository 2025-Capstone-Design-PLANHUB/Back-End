package soon.planhub.domain.team.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import soon.planhub.ControllerTestSupport;
import soon.planhub.domain.team.controller.dto.request.TeamCreateRequest;
import soon.planhub.domain.team.controller.dto.request.TeamJoinRequest;
import soon.planhub.domain.team.service.dto.request.TeamJoinServiceRequest;
import soon.planhub.global.annotation.TestMemberId;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TeamControllerTest extends ControllerTestSupport {

    private static final String BASE_URL = "/api/v1/teams";

    @TestMemberId
    @DisplayName("팀을 생성한다.")
    @Test
    void createTeam() throws Exception {
        // given
        TeamCreateRequest request = TeamCreateRequest.builder()
            .name("Test name")
            .description("Test description")
            .organizationName("Test organization")
            .build();

        given(teamService.createTeam(request.toServiceRequest(), 1L))
            .willReturn(1L);

        // expected
        mockMvc.perform(post(BASE_URL)
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").value(1));
    }

    @DisplayName("팀을 생성할 때 팀 이름은 필수값이다.")
    @Test
    void createTeamWithoutName() throws Exception {
        // given
        TeamCreateRequest request = TeamCreateRequest.builder()
            .description("Test description")
            .organizationName("Test organization")
            .build();

        // expected
        mockMvc.perform(
                post(BASE_URL)
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value(400))
            .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
            .andExpect(jsonPath("$.validation.name").value("팀 이름을 입력해주세요"));
    }

    @TestMemberId
    @DisplayName("팀에 참여한다.")
    @Test
    void joinTeam() throws Exception {
        // given
        long teamId = 1L;
        TeamJoinRequest request = TeamJoinRequest.builder()
            .invitationCode("valid-code")
            .position("BACKEND")
            .build();

        given(teamService.joinTeam(any(TeamJoinServiceRequest.class), anyLong()))
            .willReturn(teamId);

        // expected
        mockMvc.perform(
                post(BASE_URL + "/" + teamId + "/members")
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
        TeamJoinRequest request = TeamJoinRequest.builder()
            .position("BACKEND")
            .build();

        // expected
        mockMvc.perform(
                post(BASE_URL + "/" + teamId + "/members")
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