package soon.planhub.domain.team.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soon.planhub.domain.team.service.dto.request.TeamCreateServiceRequest;
import soon.planhub.global.exception.dto.ErrorDetail;
import soon.planhub.global.exception.team.IsNotAdminInOrganizationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TeamServiceTest {

    @InjectMocks
    private TeamService teamService;

    @Mock
    private TeamCreator teamCreator;

    @Mock
    private TeamValidator teamValidator;

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

}