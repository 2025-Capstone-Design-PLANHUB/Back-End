package soon.planhub.domain.task.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import soon.planhub.domain.BaseEntity;
import soon.planhub.domain.milestone.entity.Milestone;
import soon.planhub.domain.teammember.entity.TeamMember;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "tasks")
@Entity
public class Task extends BaseEntity {

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private Integer githubIssueNumber;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_member_id", nullable = false)
    private TeamMember assignee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "milestone_id", nullable = false)
    private Milestone milestone;

    @Builder.Default
    @OneToMany(mappedBy = "task")
    private Set<TaskLabelRelation> labelRelations = new HashSet<>();

    public static Task create(
        String title,
        String content,
        Integer githubIssueNumber,
        TaskStatus status,
        TeamMember assignee,
        Milestone milestone
    ) {
        return Task.builder()
            .title(title)
            .content(content)
            .githubIssueNumber(githubIssueNumber)
            .status(status)
            .assignee(assignee)
            .milestone(milestone)
            .build();
    }

    public void update(
        String title,
        String content,
        TaskStatus status,
        TeamMember assignee,
        Milestone milestone
    ) {
        this.title = title;
        this.content = content;
        this.status = status;
        this.assignee = assignee;
        this.milestone = milestone;
    }

    public void reopen() {
        this.status = TaskStatus.OPEN;
    }

    public void close() {
        this.status = TaskStatus.CLOSED;
    }

    public List<TaskLabel> getLabels() {
        return labelRelations.stream()
            .map(TaskLabelRelation::getTaskLabel)
            .toList();
    }

}