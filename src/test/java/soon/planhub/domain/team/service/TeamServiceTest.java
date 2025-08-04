package soon.planhub.domain.team.service;

import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soon.planhub.domain.team.service.dto.request.TeamCreateServiceRequest;

@ExtendWith(MockitoExtension.class)
class TeamServiceTest {

    @InjectMocks
    private TeamService teamService;

    @Mock
    private TeamCreator teamCreator;

    @DisplayName("팀을 생성한다.")
    @Test
    void createTeam() {
        // given
        Long creatorId = 1L;
        TeamCreateServiceRequest request = TeamCreateServiceRequest.builder()
            .name("Test name")
            .description("Test description")
            .organizationName("Test organization")
            .build();

        // when
        teamService.createTeam(request, creatorId);

        // then
        verify(teamCreator).createTeam(request.toInfo(), creatorId);
    }

}