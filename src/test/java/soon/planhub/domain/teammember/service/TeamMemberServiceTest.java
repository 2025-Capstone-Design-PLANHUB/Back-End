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
import soon.planhub.global.exception.common.EntityNotFoundException;
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
import static soon.planhub.global.exception.dto.ErrorDetail.*;

@ExtendWith(MockitoExtension.class)
class TeamMemberServiceTest {

    @InjectMocks
    private TeamMemberService teamMemberService;

    @Mock
    private TeamMemberAppender teamMemberAppender;

    @Mock
    private TeamValidator teamValidator;

    @Mock
    private TeamMemberValidator teamMemberValidator;

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
        var request = createTeamMemberAppendServiceRequest(teamId, invitationCode, position);

        given(teamMemberAppender.appendToMember(memberId, teamId, position)).willReturn(teamId);

        // when
        Long joinedTeamId = teamMemberService.append(request, memberId);

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
        var request = createTeamMemberAppendServiceRequest(teamId, invitationCode, position);

        willThrow(new InvalidRequest("invitationCode", INVALID_INVITATION_CODE.getMessage()))
            .given(teamValidator)
            .validateInvitationCode(eq(invitationCode), any(LocalDateTime.class));

        // expected
        assertThatThrownBy(() -> teamMemberService.append(request, memberId))
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

        willDoNothing().given(teamMemberValidator).validateTeamHasMember(anyLong(), anyLong());
        given(teamMemberReader.getTeamMembers(anyLong())).willReturn(mockResponses);

        // when
        List<TeamMemberDetailResponse> responses = teamMemberService.getTeamMembers(teamId, memberId);

        // then
        verify(teamMemberValidator).validateTeamHasMember(eq(teamId), eq(memberId));
        verify(teamMemberReader).getTeamMembers(eq(teamId));
        assertThat(responses).hasSize(2)
            .isEqualTo(mockResponses);
    }

    @DisplayName("팀원이 아닌 회원이 팀 멤버 목록을 조회하면 예외가 발생한다.")
    @Test
    void getMembersFromTeamWithoutJoining() {
        // given
        Long teamId = 1L;
        Long memberId = 1L;

        willThrow(new EntityNotFoundException(TEAM_MEMBER_NOT_FOUND))
            .given(teamMemberValidator)
            .validateTeamHasMember(anyLong(), anyLong());

        // expected
        assertThatThrownBy(() -> teamMemberService.getTeamMembers(teamId, memberId))
            .isInstanceOf(EntityNotFoundException.class)
            .hasMessage(TEAM_MEMBER_NOT_FOUND.getMessage());
        verify(teamMemberReader, never()).getTeamMembers(anyLong());
    }

    @DisplayName("팀원의 포지션을 수정한다.")
    @Test
    void updatePosition() {
        // given
        var request = getTeamMemberPositionModifyServiceRequest();

        // when
        teamMemberService.updatePosition(request, 1L);

        // then
        verify(teamMemberValidator).validateTeamHasMember(eq(request.teamId()), eq(1L));
        verify(teamMemberModifier).updatePosition(eq(request.teamMemberId()), eq(request.position()));
    }

    @DisplayName("팀원이 아닌 회원이 포지션 변경을 요청하면 예외가 발생한다.")
    @Test
    void updatePositionFromTeamWithoutJoining() {
        // given
        var request = getTeamMemberPositionModifyServiceRequest();

        willThrow(new EntityNotFoundException(TEAM_MEMBER_NOT_FOUND))
            .given(teamMemberValidator)
            .validateTeamHasMember(anyLong(), anyLong());

        // expected
        assertThatThrownBy(() -> teamMemberService.updatePosition(request, 1L))
            .isInstanceOf(EntityNotFoundException.class)
            .hasMessage(TEAM_MEMBER_NOT_FOUND.getMessage());
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
        verify(teamMemberValidator).validateTeamHasMember(eq(teamId), eq(memberId));
        verify(teamMemberModifier).updateVisibility(eq(teamId), eq(memberId), eq(isVisible));
    }

    private TeamMemberAppendServiceRequest createTeamMemberAppendServiceRequest(Long teamId, String invitationCode, String position) {
        return TeamMemberAppendServiceRequest.builder()
            .teamId(teamId)
            .invitationCode(invitationCode)
            .position(position)
            .build();
    }

    private TeamMemberPositionModifyServiceRequest getTeamMemberPositionModifyServiceRequest() {
        return TeamMemberPositionModifyServiceRequest.builder()
            .teamId(1L)
            .teamMemberId(1L)
            .position(Position.BACKEND.name())
            .build();
    }

}