package soon.planhub.domain.task.entity;

import jakarta.persistence.*;
import lombok.*;
import soon.planhub.domain.BaseEntity;
import soon.planhub.domain.project.entity.Project;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "task_templates")
@Entity
public class TaskTemplate extends BaseEntity {

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TaskType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    public static TaskTemplate create(
        String title,
        String description,
        String content,
        String type,
        Project project
    ) {
        return TaskTemplate.builder()
            .title(title)
            .description(description)
            .content(content)
            .type((type == null) ? TaskType.Custom : TaskType.from(type))
            .project(project)
            .build();
    }

    public void update(
        String title,
        String description,
        String content,
        TaskType type
    ) {
        this.title = title;
        this.description = description;
        this.content = content;
        this.type = type;
    }

}