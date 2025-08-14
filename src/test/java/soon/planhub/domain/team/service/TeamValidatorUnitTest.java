package soon.planhub.domain.team.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soon.planhub.domain.team.entity.Team;
import soon.planhub.domain.team.port.out.TeamAuthPort;
import soon.planhub.domain.team.repository.TeamRepository;
import soon.planhub.global.exception.common.InvalidRequest;
import soon.planhub.global.exception.team.IsNotAdminInOrganizationException;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class TeamValidatorUnitTest {

    @InjectMocks
    private TeamValidator teamValidator;

    @Mock
    private TeamAuthPort teamAuthPort;

    @Mock
    private TeamRepository teamRepository;

    @DisplayName("사용자가 org의 관리자인 경우 예외가 발생하지 않는다.")
    @Test
    void validateAdmin() {
        // given
        String organizationName = "test-org";
        Long creatorId = 1L;

        given(teamAuthPort.isActiveAdmin(organizationName, creatorId))
            .willReturn(true);

        // expected
        assertDoesNotThrow(() -> {
            teamValidator.validateAdminPermission(organizationName, creatorId);
        });
        verify(teamAuthPort, times(1)).isActiveAdmin(organizationName, creatorId);
    }

    @DisplayName("사용자가 org의 관리자가 아닌 경우 예외가 발생한다.")
    @Test
    void validateNotAdmin() {
        // given
        String organizationName = "test-org";
        Long creatorId = 1L;

        given(teamAuthPort.isActiveAdmin(organizationName, creatorId))
            .willReturn(false);

        // expected
        assertThrows(IsNotAdminInOrganizationException.class,
            () -> teamValidator.validateAdminPermission(organizationName, creatorId)
        );
        verify(teamAuthPort, times(1)).isActiveAdmin(organizationName, creatorId);
    }

    @DisplayName("초대코드가 유효 할 경우 예외가 발생하지 않는다.")
    @Test
    void validateInvitationCodeWithValidCode() {
        // given
        String code = "valid-code";
        LocalDateTime now = LocalDateTime.now();
        Team mockTeam = mock(Team.class);

        given(teamRepository.findTeamByValidInvitationCode(code, now))
            .willReturn(mockTeam);
        given(mockTeam.shouldRefreshInvitationCode(now))
            .willReturn(false);

        // expected
        assertDoesNotThrow(() -> teamValidator.validateInvitationCode(code, now));
        verify(teamRepository, times(1)).findTeamByValidInvitationCode(code, now);
        verify(mockTeam, times(1)).shouldRefreshInvitationCode(now);
    }

    @DisplayName("만료된 초대 코드로 검증 시 예외가 발생한다.")
    @Test
    void validateInvitationCodeWithExpiredCode() {
        // given
        String code = "expired-code";
        LocalDateTime now = LocalDateTime.now();
        Team mockTeam = mock(Team.class);

        given(teamRepository.findTeamByValidInvitationCode(code, now))
            .willReturn(mockTeam);
        given(mockTeam.shouldRefreshInvitationCode(now))
            .willReturn(true);

        // expected
        assertThrows(InvalidRequest.class,
            () -> teamValidator.validateInvitationCode(code, now)
        );
        verify(teamRepository, times(1)).findTeamByValidInvitationCode(code, now);
        verify(mockTeam, times(1)).shouldRefreshInvitationCode(now);
    }

}