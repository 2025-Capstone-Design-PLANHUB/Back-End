package soon.planhub.domain.task.entity;

import jakarta.persistence.*;
import lombok.*;
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

    public String getColor() {
        return this.color.getColor();
    }

}