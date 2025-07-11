package soon.planhub.domain.project.entity;

import jakarta.persistence.Column;
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
import soon.planhub.domain.team.entity.Team;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "projects")
@Entity
public class Project extends BaseEntity {

    @Column(nullable = false)
    private String title;

    @Column(name = "github_repository_id", nullable = false)
    private String repositoryId;

    @Column(nullable = false)
    private Long creatorId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    public static Project create(String title, String repositoryId, Long creatorId, Team team) {
        return Project.builder()
            .title(title)
            .repositoryId(repositoryId)
            .creatorId(creatorId)
            .team(team)
            .build();
    }

}