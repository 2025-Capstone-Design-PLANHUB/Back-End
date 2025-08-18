package soon.planhub.domain.task.service.template;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import soon.planhub.IntegrationTestSupport;
import soon.planhub.domain.project.entity.Project;
import soon.planhub.domain.project.repository.ProjectRepository;
import soon.planhub.domain.task.entity.TaskTemplate;
import soon.planhub.domain.task.entity.TaskType;
import soon.planhub.domain.task.repository.TaskTemplateRepository;
import soon.planhub.domain.task.service.dto.template.TaskTemplateInformation;
import soon.planhub.domain.team.entity.Team;
import soon.planhub.domain.team.repository.TeamRepository;

import static org.assertj.core.api.Assertions.assertThat;

class TaskTemplateProcessorTest extends IntegrationTestSupport {

    @Autowired
    private TaskTemplateProcessor taskTemplateProcessor;

    @Autowired
    private TaskTemplateRepository taskTemplateRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private TeamRepository teamRepository;

    @DisplayName("탬플릿을 생성한다.")
    @Test
    void createTaskTemplate() {
        // given
        Team team = Team.create("Test name", "Test description", "Test org");
        teamRepository.save(team);

        Project project = Project.create("Test title", "Test repositoryId", 1L, team);
        projectRepository.save(project);

        TaskTemplateInformation info = TaskTemplateInformation.builder()
            .title("Test title")
            .description("Test description")
            .content("Test content")
            .type(TaskType.Fix.name())
            .build();

        // when
        Long savedTaskTemplateId = taskTemplateProcessor.createTaskTemplate(info, project.getId());

        // then
        TaskTemplate savedTemplate = taskTemplateRepository.findById(savedTaskTemplateId);
        assertThat(savedTemplate).isNotNull()
            .extracting("title", "description", "content", "type")
            .containsExactly("Test title", "Test description", "Test content", TaskType.Fix);
    }

}