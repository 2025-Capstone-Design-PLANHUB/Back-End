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
import soon.planhub.global.exception.common.EntityNotFoundException;
import soon.planhub.global.exception.dto.ErrorDetail;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TaskLabelRemoverTest extends IntegrationTestSupport {

    @Autowired
    private TaskLabelRemover taskLabelRemover;

    @Autowired
    private TaskLabelRepository taskLabelRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private TeamRepository teamRepository;

    @DisplayName("라벨을 삭제한다.")
    @Test
    void delete() {
        // given
        Team team = Team.create("Test name", "Test description", "Test organization");
        teamRepository.save(team);

        Project project = Project.create("Test title", "Test repoId", 1L, team);
        projectRepository.save(project);

        TaskLabel label = TaskLabel.create("Test title", "Test description", "#FFFFFF", project);
        taskLabelRepository.save(label);

        // when
        taskLabelRemover.deleteLabel(label.getId());

        // then
        assertThatThrownBy(() -> taskLabelRepository.findById(label.getId()))
            .isInstanceOfAny(EntityNotFoundException.class)
            .hasMessage(ErrorDetail.TASK_LABEL_NOT_FOUND.getMessage());
    }

}