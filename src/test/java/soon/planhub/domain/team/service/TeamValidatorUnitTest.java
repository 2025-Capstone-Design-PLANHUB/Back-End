package soon.planhub.domain.team.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soon.planhub.domain.team.port.out.TeamAuthPort;
import soon.planhub.global.exception.team.IsNotAdminInOrganizationException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class TeamValidatorUnitTest {

    @InjectMocks
    private TeamValidator teamValidator;

    @Mock
    private TeamAuthPort teamAuthPort;

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

}