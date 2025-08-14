package soon.planhub.domain.team.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soon.planhub.domain.team.service.dto.request.TeamCreateServiceRequest;
import soon.planhub.domain.team.service.dto.request.TeamJoinServiceRequest;
import soon.planhub.domain.teammember.entity.Position;
import soon.planhub.domain.teammember.port.out.TeamMemberPort;
import soon.planhub.domain.teammember.service.TeamMemberAppender;
import soon.planhub.global.exception.common.InvalidRequest;
import soon.planhub.global.exception.dto.ErrorDetail;
import soon.planhub.global.exception.team.IsNotAdminInOrganizationException;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static soon.planhub.global.exception.dto.ErrorDetail.INVALID_INVITATION_CODE;
import static soon.planhub.global.exception.dto.ErrorDetail.INVALID_REQUEST;

@ExtendWith(MockitoExtension.class)
class TeamServiceTest {

    @InjectMocks
    private TeamService teamService;

    @Mock
    private TeamCreator teamCreator;

    @Mock
    private TeamValidator teamValidator;

    @Mock
    private TeamMemberAppender teamMemberAppender;

    @Mock
    private TeamMemberPort teamMemberPort;

    @DisplayName("팀을 생성한다.")
    @Test
    void createTeam() {
        // given
        Long creatorId = 1L;
        Long expectedTeamId = 100L;
        TeamCreateServiceRequest request = TeamCreateServiceRequest.builder()
            .name("Test name")
            .description("Test description")
            .organizationName("Test organization")
            .build();

        given(teamCreator.createTeam(request.toInfo(), creatorId))
            .willReturn(expectedTeamId);

        // when
        Long teamId = teamService.createTeam(request, creatorId);

        // then
        verify(teamValidator).validateAdminPermission(request.organizationName(), creatorId);
        verify(teamCreator).createTeam(request.toInfo(), creatorId);
        assertThat(teamId).isEqualTo(expectedTeamId);
    }

    @DisplayName("관리자 권한이 없다면 예외가 발생한다.")
    @Test
    void createTeamWhenMemberAdminPermission() {
        // given
        Long creatorId = 1L;
        TeamCreateServiceRequest request = TeamCreateServiceRequest.builder()
            .name("Test name")
            .description("Test description")
            .organizationName("Test organization")
            .build();

        willThrow(new IsNotAdminInOrganizationException(ErrorDetail.IS_NOT_ADMIN_IN_ORGANIZATION))
            .given(teamValidator)
            .validateAdminPermission(request.organizationName(), creatorId);

        // expected
        assertThatThrownBy(() -> teamService.createTeam(request, creatorId))
            .isInstanceOf(IsNotAdminInOrganizationException.class)
            .hasMessage(ErrorDetail.IS_NOT_ADMIN_IN_ORGANIZATION.getMessage());

        verify(teamValidator).validateAdminPermission(request.organizationName(), creatorId);
        verify(teamCreator, never()).createTeam(request.toInfo(), creatorId);
    }

    @DisplayName("팀에 참여한다.")
    @Test
    void joinTeam() {
        // given
        Long memberId = 1L;
        Long teamId = 1L;
        String position = Position.BACKEND.name();
        String invitationCode = "valid-code";
        TeamJoinServiceRequest request = createTeamJoinServiceRequest(teamId, invitationCode, position);

        given(teamMemberAppender.appendToMember(memberId, teamId, position)).willReturn(teamId);

        // when
        Long joinedTeamId = teamService.joinTeam(request, memberId);

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
        TeamJoinServiceRequest request = createTeamJoinServiceRequest(teamId, invitationCode, position);

        willThrow(new InvalidRequest("invitationCode", INVALID_INVITATION_CODE.getMessage()))
            .given(teamValidator)
            .validateInvitationCode(eq(invitationCode), any(LocalDateTime.class));

        // expected
        assertThatThrownBy(() -> teamService.joinTeam(request, memberId))
            .isInstanceOf(InvalidRequest.class)
            .hasMessage(INVALID_REQUEST.getMessage());

        verify(teamValidator).validateInvitationCode(eq(invitationCode), any(LocalDateTime.class));
        verify(teamMemberPort, never()).appendMemberToOrg(teamId, memberId);
        verify(teamMemberAppender, never()).appendToMember(memberId, teamId, position);
    }

    private TeamJoinServiceRequest createTeamJoinServiceRequest(Long teamId, String invitationCode, String position) {
        return TeamJoinServiceRequest.builder()
            .teamId(teamId)
            .invitationCode(invitationCode)
            .position(position)
            .build();
    }

}