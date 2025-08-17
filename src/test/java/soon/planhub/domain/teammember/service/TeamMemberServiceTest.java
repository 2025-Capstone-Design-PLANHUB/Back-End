package soon.planhub.domain.teammember.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soon.planhub.domain.team.service.TeamValidator;
import soon.planhub.domain.team.service.dto.request.TeamMemberAppendServiceRequest;
import soon.planhub.domain.teammember.entity.Position;
import soon.planhub.domain.teammember.port.out.TeamMemberPort;
import soon.planhub.global.exception.common.InvalidRequest;

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
class TeamMemberServiceTest {

    @InjectMocks
    private TeamMemberService teamMemberService;

    @Mock
    private TeamMemberAppender teamMemberAppender;

    @Mock
    private TeamValidator teamValidator;

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
        TeamMemberAppendServiceRequest request = createTeamMemberAppendServiceRequest(teamId, invitationCode, position);

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
        TeamMemberAppendServiceRequest request = createTeamMemberAppendServiceRequest(teamId, invitationCode, position);

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

    private TeamMemberAppendServiceRequest createTeamMemberAppendServiceRequest(Long teamId, String invitationCode, String position) {
        return TeamMemberAppendServiceRequest.builder()
            .teamId(teamId)
            .invitationCode(invitationCode)
            .position(position)
            .build();
    }

}