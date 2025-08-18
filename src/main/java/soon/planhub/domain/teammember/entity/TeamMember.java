package soon.planhub.domain.teammember.entity;

import jakarta.persistence.*;
import lombok.*;
import soon.planhub.domain.BaseEntity;
import soon.planhub.domain.member.entity.Member;
import soon.planhub.domain.team.entity.Team;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "team_members")
@Entity
public class TeamMember extends BaseEntity {

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Position position;

    @Column(nullable = false)
    private boolean isVisible;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    public static TeamMember createLeader(Member member, Team team) {
        return TeamMember.builder()
            .role(Role.ROLE_LEADER)
            .position(Position.NONE)
            .member(member)
            .team(team)
            .isVisible(true)
            .build();
    }

    public static TeamMember createMember(Member member, Team team, String position) {
        return TeamMember.builder()
            .role(Role.ROLE_MEMBER)
            .position((position == null) ? Position.NONE : Position.from(position))
            .member(member)
            .team(team)
            .isVisible(true)
            .build();
    }

    public void updatePosition(String position) {
        this.position = (position == null) ? Position.NONE : Position.from(position);
    }

    public void updateVisibility(boolean isVisible) {
        this.isVisible = isVisible;
    }

    public void updateRole(Role role) {
        this.role = role;
    }

}