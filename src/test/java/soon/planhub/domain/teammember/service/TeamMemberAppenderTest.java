package soon.planhub.domain.teammember.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import soon.planhub.IntegrationTestSupport;
import soon.planhub.domain.member.entity.Member;
import soon.planhub.domain.member.repository.MemberRepository;
import soon.planhub.domain.team.entity.Team;
import soon.planhub.domain.team.repository.TeamRepository;
import soon.planhub.domain.teammember.entity.Position;
import soon.planhub.domain.teammember.entity.Role;
import soon.planhub.domain.teammember.entity.TeamMember;
import soon.planhub.domain.teammember.repository.TeamMemberRepository;

import static org.assertj.core.api.Assertions.assertThat;

class TeamMemberAppenderTest extends IntegrationTestSupport {

    @Autowired
    private TeamMemberAppender teamMemberAppender;

    @Autowired
    private TeamMemberRepository teamMemberRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TeamRepository teamRepository;

    @AfterEach
    void tearDown() {
        teamMemberRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
        teamRepository.deleteAllInBatch();
    }

    @DisplayName("팀에 리더를 추가한다.")
    @Test
    void appendToLeader() {
        // given
        Team team = Team.create("Test Team", "Test Description", "Test Organization");
        teamRepository.save(team);

        Member member = Member.create("Test email", "Test nickname", "Test profile image");
        memberRepository.save(member);

        // when
        teamMemberAppender.appendToLeader(member, team);

        // then
        TeamMember teamMemberByTeam = teamMemberRepository.findByTeamId(team.getId());
        assertThat(teamMemberByTeam.getRole())
            .isEqualTo(Role.ROLE_LEADER);
    }

    @DisplayName("팀에 멤버를 추가한다.")
    @Test
    void appendToMember() {
        // given
        Team team = Team.create("Test Team", "Test Description", "Test Organization");
        teamRepository.save(team);

        Member member = Member.create("Test email", "Test nickname", "Test profile image");
        memberRepository.save(member);

        // when
        teamMemberAppender.appendToMember(member, team, Position.BACKEND);

        // then
        TeamMember teamMemberByTeam = teamMemberRepository.findByTeamId(team.getId());
        assertThat(teamMemberByTeam)
            .extracting("role", "position")
            .containsExactly(Role.ROLE_MEMBER, Position.BACKEND);
    }

}