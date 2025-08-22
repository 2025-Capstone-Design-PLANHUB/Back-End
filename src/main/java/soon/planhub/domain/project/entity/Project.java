package soon.planhub.domain.project.entity;

import jakarta.persistence.*;
import lombok.*;
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

    public String getOrganizationName() {
        return team.getOrganizationName();
    }

}