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
import soon.planhub.domain.teammember.entity.TeamMember;
import soon.planhub.domain.teammember.repository.TeamMemberRepository;
import soon.springtestutil.querycount.assertion.QueryCounterAssertion;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TeamMemberModifierTest extends IntegrationTestSupport {

    @Autowired
    private TeamMemberModifier teamMemberModifier;

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

    @DisplayName("포지션을 변경한다.")
    @Test
    void updatePosition() {
        // given
        Team team = Team.create("Test Team", "Test Description", "Test Organization");
        teamRepository.save(team);

        Member member = Member.create("Test email", "Test nickname", "Test profile image");
        memberRepository.save(member);

        TeamMember teamMember = TeamMember.createMember(member, team, Position.BACKEND.name());
        teamMemberRepository.save(teamMember);

        // when
        teamMemberModifier.updatePosition(teamMember.getId(), Position.FRONTEND.name());

        // then
        TeamMember updatedTeamMember = teamMemberRepository.findById(teamMember.getId());
        assertThat(updatedTeamMember.getPosition()).isEqualTo(Position.FRONTEND);
    }

    @DisplayName("포지션을 변경할 때, 존재하지 않는 값을 전달하면 예외가 발생한다.")
    @Test
    void updatePositionToNone() {
        // given
        Team team = Team.create("Test Team", "Test Description", "Test Organization");
        teamRepository.save(team);

        Member member = Member.create("Test email", "Test nickname", "Test profile image");
        memberRepository.save(member);

        TeamMember teamMember = TeamMember.createMember(member, team, Position.BACKEND.name());
        teamMemberRepository.save(teamMember);

        QueryCounterAssertion.assertCounts()
            .forTables("team_members")
            .insert(1)
            .verify();

        // expected
        assertThrows(IllegalArgumentException.class,
            () -> teamMemberModifier.updatePosition(teamMember.getId(), "INVALID_POSITION"));
    }

}