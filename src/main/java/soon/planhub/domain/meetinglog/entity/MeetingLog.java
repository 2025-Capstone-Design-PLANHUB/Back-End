package soon.planhub.domain.meetinglog.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import soon.planhub.domain.BaseEntity;
import soon.planhub.domain.member.entity.Member;
import soon.planhub.domain.team.entity.Team;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "meeting_logs")
@Entity
public class MeetingLog extends BaseEntity {

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    public static MeetingLog create(String content, Member member, Team team, LocalDate now) {
        return MeetingLog.builder()
            .content(content)
            .title(generateTitle(team, now))
            .member(member)
            .team(team)
            .build();
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

    private static String generateTitle(Team team, LocalDate now) {
        String formattedDate = now.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
        return "회의록 - " + team.getName() + " (" + formattedDate + ")";
    }

}