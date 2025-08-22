package soon.planhub.domain.task.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import soon.planhub.IntegrationTestSupport;
import soon.planhub.domain.project.entity.Project;
import soon.planhub.domain.project.repository.ProjectRepository;
import soon.planhub.domain.task.entity.TaskLabel;
import soon.planhub.domain.task.repository.TaskLabelRepository;
import soon.planhub.domain.task.service.dto.label.TaskLabelInformation;
import soon.planhub.domain.task.service.label.TaskLabelCreator;
import soon.planhub.domain.team.entity.Team;
import soon.planhub.domain.team.repository.TeamRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TaskLabelCreatorTest extends IntegrationTestSupport {

    @Autowired
    private TaskLabelCreator taskLabelCreator;

    @Autowired
    private TaskLabelRepository taskLabelRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private TeamRepository teamRepository;

    @DisplayName("라벨을 생성한다.")
    @Test
    void createLabel() {
        // given
        Team team = Team.create("Test name", "Test description", "Test organization");
        teamRepository.save(team);

        Project project = Project.create("Test title", "Test repoId", 1L, team);
        projectRepository.save(project);

        TaskLabelInformation info = TaskLabelInformation.builder()
            .title("Test Label")
            .description("Test Description")
            .color("#FFFFFF")
            .build();

        // when
        Long labelId = taskLabelCreator.createLabel(info, project.getId());

        // then
        TaskLabel label = taskLabelRepository.findById(labelId);
        assertThat(label).isNotNull()
            .extracting("title", "description")
            .containsExactly("Test Label", "Test Description");

        assertThat(label.getColor()).isEqualTo("FFFFFF");
    }

    @DisplayName("라벨을 생성할 때 유효하지 않은 색상 코드를 입력하면 예외가 발생한다.")
    @Test
    void createLabelWithInvalidColor() {
        // given
        Team team = Team.create("Test name", "Test description", "Test organization");
        teamRepository.save(team);

        Project project = Project.create("Test title", "Test repoId", 1L, team);
        projectRepository.save(project);

        TaskLabelInformation info = TaskLabelInformation.builder()
            .title("Invalid Color Label")
            .description("This label has an invalid color")
            .color("invalid-color")
            .build();

        // expected
        assertThatThrownBy(() -> taskLabelCreator.createLabel(info, project.getId()))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("유효하지 않은 색상 코드입니다");
    }

}