package soon.planhub.domain.teammember.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import soon.planhub.ControllerTestSupport;
import soon.planhub.domain.teammember.controller.dto.request.TeamMemberAppendRequest;
import soon.planhub.domain.teammember.controller.dto.request.TeamMemberPositionModifyRequest;
import soon.planhub.domain.teammember.service.dto.request.TeamMemberAppendServiceRequest;
import soon.planhub.domain.teammember.service.dto.response.TeamMemberDetailResponse;
import soon.planhub.global.annotation.TestMemberId;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

    @TestMemberId
    @DisplayName("팀원 목록을 조회한다.")
    @Test
    void getTeamMembers() throws Exception {
        // given
        long teamId = 1L;

        given(teamMemberService.getTeamMembers(anyLong(), anyLong()))
            .willReturn(List.of(
                TeamMemberDetailResponse.builder()
                    .nickname("test1")
                    .build(),
                TeamMemberDetailResponse.builder()
                    .nickname("test2")
                    .build()
            ));

        // expected
        mockMvc.perform(
                get(BASE_URL, teamId)
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].nickname").value("test1"))
            .andExpect(jsonPath("$[1].nickname").value("test2"));
    }

    @TestMemberId
    @DisplayName("팀원의 포지션을 변경한다.")
    @Test
    void updatePosition() throws Exception {
        // given
        long teamId = 1L;
        var request = TeamMemberPositionModifyRequest.builder()
            .teamMemberId(1L)
            .position("FRONTEND")
            .build();

        // expected
        mockMvc.perform(
                patch(BASE_URL + "/position", teamId)
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isNoContent());
    }

    @DisplayName("팀원의 포지션을 변경할 때 포지션을 필수값이다")
    @Test
    void updatePositionWithoutPosition() throws Exception {
        // given
        long teamId = 1L;
        var request = TeamMemberPositionModifyRequest.builder()
            .teamMemberId(1L)
            .build();

        // expected
        mockMvc.perform(
                patch(BASE_URL + "/position", teamId)
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value(400))
            .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
            .andExpect(jsonPath("$.validation.position").value("포지션은 비어있을 수 없습니다."));
    }

    @TestMemberId
    @DisplayName("본인의 팀에 대한 가시성을 변경한다.")
    @Test
    void updateVisibility() throws Exception {
        // given
        long teamId = 1L;
        boolean visible = true;

        // expected
        mockMvc.perform(
                patch(BASE_URL + "/visibility", teamId)
                    .param("visible", String.valueOf(visible))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isNoContent());
    }

}