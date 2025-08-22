package soon.planhub.domain.task.service.label;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import soon.planhub.IntegrationTestSupport;
import soon.planhub.domain.project.entity.Project;
import soon.planhub.domain.project.repository.ProjectRepository;
import soon.planhub.domain.task.entity.TaskLabel;
import soon.planhub.domain.task.repository.TaskLabelRepository;
import soon.planhub.domain.team.entity.Team;
import soon.planhub.domain.team.repository.TeamRepository;
import soon.planhub.global.exception.common.InvalidRequest;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TaskLabelValidatorTest extends IntegrationTestSupport {

    @Autowired
    private TaskLabelValidator taskLabelValidator;

    @Autowired
    private TaskLabelRepository taskLabelRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private TeamRepository teamRepository;

    @DisplayName("이미 존재하는 라벨이라면 예외가 발생한다")
    @Test
    void validateLabelNotExists() {
        // given
        Team team = Team.create("Test name", "Test description", "Test organization");
        teamRepository.save(team);

        Project project = Project.create("Test title", "Test repoId", 1L, team);
        projectRepository.save(project);

        TaskLabel label = TaskLabel.create("Test title", "Test description", "#FFFFFF", project);
        taskLabelRepository.save(label);

        // expected
        assertThatThrownBy(() -> taskLabelValidator.validateLabelNotExists(label.getTitle(), project.getId()))
            .isInstanceOf(InvalidRequest.class)
            .hasMessage("잘못된 요청입니다.");
    }

}