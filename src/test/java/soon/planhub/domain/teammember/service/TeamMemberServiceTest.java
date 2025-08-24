package soon.planhub.domain.teammember.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soon.planhub.domain.team.service.TeamValidator;
import soon.planhub.domain.teammember.entity.Position;
import soon.planhub.domain.teammember.port.out.TeamMemberPort;
import soon.planhub.domain.teammember.service.dto.request.TeamMemberAppendServiceRequest;
import soon.planhub.domain.teammember.service.dto.request.TeamMemberPositionModifyServiceRequest;
import soon.planhub.domain.teammember.service.dto.response.TeamMemberDetailResponse;
import soon.planhub.global.exception.common.InvalidRequest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static soon.planhub.global.exception.dto.ErrorDetail.INVALID_INVITATION_CODE;
import static soon.planhub.global.exception.dto.ErrorDetail.INVALID_REQUEST;

@ExtendWith(MockitoExtension.class)
class TeamMemberServiceTest {

    @InjectMocks
    private TeamMemberService teamMemberService;

    @Mock
    private TeamMemberAppender teamMemberAppender;

    @Mock
    private TeamValidator teamValidator;

    @Mock
    private TeamMemberReader teamMemberReader;

    @Mock
    private TeamMemberModifier teamMemberModifier;

    @Mock
    private TeamMemberPort teamMemberPort;

    @DisplayName("팀에 멤버를 추가한다.")
    @Test
    void appendTeam() {
        // given
        Long memberId = 1L;
        Long teamId = 1L;
        String position = Position.BACKEND.name();
        String invitationCode = "valid-code";
        var request = createTeamMemberAppendServiceRequest(invitationCode, position);

        given(teamMemberAppender.appendToMember(memberId, teamId, position)).willReturn(teamId);

        // when
        Long joinedTeamId = teamMemberService.append(teamId, memberId, request);

        // then
        verify(teamValidator).validateInvitationCode(eq(invitationCode), any(LocalDateTime.class));
        verify(teamMemberPort).appendMemberToOrg(teamId, memberId);
        verify(teamMemberAppender).appendToMember(memberId, teamId, position);
        assertThat(joinedTeamId).isEqualTo(teamId);
    }

    @DisplayName("초대코드가 만료되어있다면 예외가 발생한다.")
    @Test
    void joinTeamWithInvitationCodeExpired() {
        // given
        Long memberId = 1L;
        Long teamId = 1L;
        String position = Position.BACKEND.name();
        String invitationCode = "expired-code";
        var request = createTeamMemberAppendServiceRequest(invitationCode, position);

        willThrow(new InvalidRequest("invitationCode", INVALID_INVITATION_CODE.getMessage()))
            .given(teamValidator)
            .validateInvitationCode(eq(invitationCode), any(LocalDateTime.class));

        // expected
        assertThatThrownBy(() -> teamMemberService.append(teamId, memberId, request))
            .isInstanceOf(InvalidRequest.class)
            .hasMessage(INVALID_REQUEST.getMessage());

        verify(teamValidator).validateInvitationCode(eq(invitationCode), any(LocalDateTime.class));
        verify(teamMemberPort, never()).appendMemberToOrg(teamId, memberId);
        verify(teamMemberAppender, never()).appendToMember(memberId, teamId, position);
    }

    @DisplayName("팀에 속한 멤버 목록을 조회한다.")
    @Test
    void getTeamMembersSuccessfully() {
        // given
        Long teamId = 1L;
        Long memberId = 1L;
        List<TeamMemberDetailResponse> mockResponses = List.of(
            TeamMemberDetailResponse.builder().nickname("test1").build(),
            TeamMemberDetailResponse.builder().nickname("test2").build()
        );

        given(teamMemberReader.getTeamMembers(anyLong())).willReturn(mockResponses);

        // when
        List<TeamMemberDetailResponse> responses = teamMemberService.getTeamMembers(teamId, memberId);

        // then
        verify(teamMemberReader).getTeamMembers(eq(teamId));
        assertThat(responses).hasSize(2)
            .isEqualTo(mockResponses);
    }

    @DisplayName("팀원의 포지션을 수정한다.")
    @Test
    void updatePosition() {
        // given
        var request = getTeamMemberPositionModifyServiceRequest();

        // when
        teamMemberService.updatePosition(1L, 1L, request);

        // then
        verify(teamMemberModifier).updatePosition(eq(request.teamMemberId()), eq(request.position()));
    }

    @DisplayName("팀원의 가시성을 수정한다.")
    @Test
    void updateVisibility() {
        // given
        long memberId = 1L;
        long teamId = 1L;
        boolean isVisible = false;

        // when
        teamMemberService.updateVisibility(teamId, memberId, isVisible);

        // then
        verify(teamMemberModifier).updateVisibility(eq(teamId), eq(memberId), eq(isVisible));
    }

    private TeamMemberAppendServiceRequest createTeamMemberAppendServiceRequest(String invitationCode, String position) {
        return TeamMemberAppendServiceRequest.builder()
            .invitationCode(invitationCode)
            .position(position)
            .build();
    }

    private TeamMemberPositionModifyServiceRequest getTeamMemberPositionModifyServiceRequest() {
        return TeamMemberPositionModifyServiceRequest.builder()
            .teamMemberId(1L)
            .position(Position.BACKEND.name())
            .build();
    }

}