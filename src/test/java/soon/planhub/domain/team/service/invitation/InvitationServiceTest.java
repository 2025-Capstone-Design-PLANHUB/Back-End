package soon.planhub.domain.team.service.invitation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soon.planhub.domain.teammember.service.TeamMemberValidator;
import soon.planhub.global.exception.common.EntityNotFoundException;
import soon.planhub.global.exception.dto.ErrorDetail;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class InvitationServiceTest {

    @InjectMocks
    private InvitationService invitationService;

    @Mock
    private InvitationCodeGenerator codeGenerator;

    @Mock
    private TeamMemberValidator teamMemberValidator;

    @DisplayName("팀 멤버가 초대 코드 생성 요청시 초대 코드가 생성된다.")
    @Test
    void generateInvitationCode() {
        // given
        Long teamId = 1L;
        Long memberId = 1L;
        String expectedCode = "EXPECTED";

        given(codeGenerator.generateInvitationCode(teamId))
            .willReturn(expectedCode);

        // when
        String code = invitationService.generateInvitationCode(teamId, memberId);

        // then
        verify(teamMemberValidator).validateTeamHasMember(teamId, memberId);
        verify(codeGenerator).generateInvitationCode(teamId);
        assertThat(code).isEqualTo(expectedCode);
    }

    @DisplayName("팀 멤버가 아닌 경우 예외가 발생한다")
    @Test
    void generateInvitationCodeWhenNotTeamMember() {
        // given
        Long teamId = 1L;
        Long memberId = 1L;

        willThrow(new EntityNotFoundException(ErrorDetail.TEAM_MEMBER_NOT_FOUND))
            .given(teamMemberValidator).validateTeamHasMember(teamId, memberId);

        // expected
        assertThatThrownBy(() -> invitationService.generateInvitationCode(teamId, memberId))
            .isInstanceOf(EntityNotFoundException.class)
            .hasMessage(ErrorDetail.TEAM_MEMBER_NOT_FOUND.getMessage());

        verify(teamMemberValidator).validateTeamHasMember(teamId, memberId);
        verify(codeGenerator, never()).generateInvitationCode(teamId);
    }

}