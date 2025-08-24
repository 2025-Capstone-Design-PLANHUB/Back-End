package soon.planhub.domain.task.service.label;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import soon.planhub.IntegrationTestSupport;
import soon.planhub.domain.project.entity.Project;
import soon.planhub.domain.project.repository.ProjectRepository;
import soon.planhub.domain.task.entity.TaskLabel;
import soon.planhub.domain.task.repository.TaskLabelRepository;
import soon.planhub.domain.task.service.dto.label.response.TaskLabelDetailResponse;
import soon.planhub.domain.team.entity.Team;
import soon.planhub.domain.team.repository.TeamRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

class TaskLabelReaderTest extends IntegrationTestSupport {

    @Autowired
    private TaskLabelReader taskLabelReader;

    @Autowired
    private TaskLabelRepository taskLabelRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private TeamRepository teamRepository;

    @DisplayName("프로젝트에 연결된 모든 라벨을 조회한다.")
    @Test
    void readLabels() {
        // given
        Team team = Team.create("Test name", "Test description", "Test organization");
        teamRepository.save(team);

        Project project = Project.create("Test title", "Test repoId", 1L, team);
        projectRepository.save(project);

        TaskLabel label1 = TaskLabel.create("Test title1", "Test description1", "#FFFFFF", project);
        TaskLabel label2 = TaskLabel.create("Test title2", "Test description2", "#FFFFFF", project);
        TaskLabel label3 = TaskLabel.create("Test title3", "Test description3", "#FFFFFF", project);
        taskLabelRepository.saveAll(List.of(label1, label2, label3));

        List<TaskLabelDetailResponse> apiLabels = List.of(
            createApiLabel("Test title1", "#FFFFFF", "Test description1"),
            createApiLabel("Test title2", "#FFFFFF", "Test description2"),
            createApiLabel("Test title3", "#FFFFFF", "Test description3")
        );

        // when
        List<TaskLabelDetailResponse> labels = taskLabelReader.readLabels(project.getId(), apiLabels);

        // then
        assertThat(labels).hasSize(3)
            .extracting("labelId", "name", "description")
            .containsExactlyInAnyOrder(
                tuple(label1.getId(), "Test title1", "Test description1"),
                tuple(label2.getId(), "Test title2", "Test description2"),
                tuple(label3.getId(), "Test title3", "Test description3")
            );
    }

    @DisplayName("DB에 존재하지 않는 라벨은 제외하고 조회한다.")
    @Test
    void readLabelsWithoutNonExistentLabel() {
        // given
        Team team = Team.create("Test name", "Test description", "Test organization");
        teamRepository.save(team);

        Project project = Project.create("Test title", "Test repoId", 1L, team);
        projectRepository.save(project);

        TaskLabel label1 = TaskLabel.create("Test title1", "Test description1", "#FFFFFF", project);
        TaskLabel label2 = TaskLabel.create("Test title2", "Test description2", "#FFFFFF", project);
        taskLabelRepository.saveAll(List.of(label1, label2));

        List<TaskLabelDetailResponse> apiLabels = List.of(
            createApiLabel("Test title1", "#FFFFFF", "Test description1"),
            createApiLabel("Test title2", "#FFFFFF", "Test description2"),
            createApiLabel("Non-existent label", "#000000", "Non-existent description")
        );

        // when
        List<TaskLabelDetailResponse> labels = taskLabelReader.readLabels(project.getId(), apiLabels);

        // then
        assertThat(labels).hasSize(2)
            .extracting("labelId", "name", "description")
            .containsExactlyInAnyOrder(
                tuple(label1.getId(), "Test title1", "Test description1"),
                tuple(label2.getId(), "Test title2", "Test description2")
            );
    }

    private TaskLabelDetailResponse createApiLabel(String name, String color, String description) {
        return TaskLabelDetailResponse.builder()
            .name(name)
            .color(color)
            .description(description)
            .build();
    }

}