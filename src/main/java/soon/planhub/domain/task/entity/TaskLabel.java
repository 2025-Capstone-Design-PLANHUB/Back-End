package soon.planhub.domain.task.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import soon.planhub.domain.BaseEntity;
import soon.planhub.domain.project.entity.Project;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "task_labels")
@Entity
public class TaskLabel extends BaseEntity {

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Embedded
    private LabelColor color;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    public static TaskLabel create(
        String title,
        String description,
        String color,
        Project project
    ) {
        return TaskLabel.builder()
            .title(title)
            .description(description)
            .color(new LabelColor(color))
            .project(project)
            .build();
    }

    public void update(
        String title,
        String description,
        String color
    ) {
        this.title = title;
        this.description = description;
        this.color = new LabelColor(color);
    }

}