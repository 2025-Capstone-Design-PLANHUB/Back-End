package soon.planhub.domain.task.service.template;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soon.planhub.domain.task.service.dto.template.request.TaskTemplateCreateServiceRequest;
import soon.planhub.domain.teammember.service.TeamMemberValidator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TaskTemplateServiceTest {

    @InjectMocks
    private TaskTemplateService taskTemplateService;

    @Mock
    private TaskTemplateProcessor taskTemplateProcessor;

    @Mock
    private TeamMemberValidator teamMemberValidator;

    @DisplayName("템플릿을 생성한다.")
    @Test
    void createTaskTemplate() {
        // given
        long projectId = 1L;
        var request = TaskTemplateCreateServiceRequest.builder()
            .teamId(1L)
            .projectId(projectId)
            .title("Test title")
            .description("Test description")
            .content("Test content")
            .type("FIX")
            .build();

        given(taskTemplateProcessor.createTaskTemplate(request.toInfo(), projectId))
            .willReturn(1L);

        // when
        Long savedTemplateId = taskTemplateService.create(request, 1L);

        // then
        verify(teamMemberValidator).validateTeamHasMember(request.teamId(), 1L);
        verify(taskTemplateProcessor).createTaskTemplate(request.toInfo(), projectId);
        assertThat(savedTemplateId).isEqualTo(1L);
    }

}