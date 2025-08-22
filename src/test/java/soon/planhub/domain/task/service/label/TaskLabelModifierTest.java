package soon.planhub.domain.task.service.label;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import soon.planhub.IntegrationTestSupport;
import soon.planhub.domain.project.entity.Project;
import soon.planhub.domain.project.repository.ProjectRepository;
import soon.planhub.domain.task.entity.TaskLabel;
import soon.planhub.domain.task.repository.TaskLabelRepository;
import soon.planhub.domain.task.service.dto.label.TaskLabelInformation;
import soon.planhub.domain.team.entity.Team;
import soon.planhub.domain.team.repository.TeamRepository;

import static org.assertj.core.api.Assertions.assertThat;

class TaskLabelModifierTest extends IntegrationTestSupport {

    @Autowired
    private TaskLabelModifier taskLabelModifier;

    @Autowired
    private TaskLabelRepository taskLabelRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private TeamRepository teamRepository;

    @DisplayName("라벨을 수정한다.")
    @Test
    void updateTaskLabel() {
        // given
        Team team = Team.create("Test name", "Test description", "Test organization");
        teamRepository.save(team);

        Project project = Project.create("Test title", "Test repoId", 1L, team);
        projectRepository.save(project);

        TaskLabel label = TaskLabel.create("Test title", "Test description", "#FFFFFF", project);
        taskLabelRepository.save(label);

        TaskLabelInformation info = TaskLabelInformation.builder()
            .title("Test Label")
            .description("Test Description")
            .color("#000000")
            .build();

        // when
        taskLabelModifier.updateLabel(info, label.getId());

        // then
        TaskLabel updatedLabel = taskLabelRepository.findById(label.getId());
        assertThat(updatedLabel).isNotNull()
            .extracting("title", "description")
            .containsExactly("Test Label", "Test Description");
    }

}