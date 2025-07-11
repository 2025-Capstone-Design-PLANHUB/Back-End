package soon.planhub.domain.milestone.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "milestones")
@Entity
public class Milestone extends BaseEntity {

    @Embedded
    private MilestoneInfo info;

    @Column(nullable = false)
    private Long creatorId;

    @Embedded
    private MilestoneSchedule schedule;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MilestoneStatus status;

    @Column(nullable = false)
    private Integer githubMilestoneId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    public static Milestone create(
        MilestoneInfo info,
        MilestoneSchedule schedule,
        Long creatorId,
        int githubMilestoneId,
        Project project
    ) {
        return Milestone.builder()
            .info(info)
            .schedule(schedule)
            .creatorId(creatorId)
            .status(MilestoneStatus.NOT_STARTED)
            .githubMilestoneId(githubMilestoneId)
            .project(project)
            .build();
    }

    public void updateInfo(MilestoneInfo info) {
        this.info = info;
    }

    public void updateSchedule(MilestoneSchedule schedule) {
        this.schedule = schedule;
    }

    public void updateStatus(MilestoneStatus status) {
        this.status = status;
    }

}