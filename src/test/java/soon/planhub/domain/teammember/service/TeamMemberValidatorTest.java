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
import soon.planhub.global.exception.common.EntityNotFoundException;
import soon.planhub.global.exception.dto.ErrorDetail;
import soon.planhub.global.exception.team.LeaderAlreadyExistsException;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TeamMemberValidatorTest extends IntegrationTestSupport {

    @Autowired
    private TeamMemberValidator teamMemberValidator;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private TeamMemberRepository teamMemberRepository;

    @Autowired
    private MemberRepository memberRepository;

    @AfterEach
    void tearDown() {
        teamMemberRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
        teamRepository.deleteAllInBatch();
    }

    @DisplayName("리더가 이미 존재하는 경우 예외가 발생한다")
    @Test
    void validateTeamHasNoLeaderThrowsException() {
        // given
        Team team = Team.create("Test Team", "Test Description", "Test Organization");
        teamRepository.save(team);

        Member member = Member.create("Test email", "Test nickname", "Test profile image");
        memberRepository.save(member);

        TeamMember leader = TeamMember.createLeader(member, team);
        teamMemberRepository.save(leader);

        // expected
        assertThatThrownBy(() -> teamMemberValidator.validateTeamHasNoLeader(team.getId()))
            .isInstanceOf(LeaderAlreadyExistsException.class)
            .hasMessage(ErrorDetail.TEAM_LEADER_ALREADY_EXISTS.getMessage());
    }

    @DisplayName("리더가 존재하지 않는 경우 예외가 발생하지 않는다")
    @Test
    void validateTeamHasNoLeader() {
        // given
        Team team = Team.create("Test Team", "Test Description", "Test Organization");
        teamRepository.save(team);

        Member member = Member.create("Test email", "Test nickname", "Test profile image");
        memberRepository.save(member);

        // excepted
        assertThatCode(() -> teamMemberValidator.validateTeamHasNoLeader(team.getId()))
            .doesNotThrowAnyException();
    }

    @DisplayName("팀에 해당 멤버가 존재하지 않는 경우 예외가 발생한다")
    @Test
    void validateTeamHasMemberThrowsException() {
        // given
        Team team = Team.create("Test Team", "Test Description", "Test Organization");
        teamRepository.save(team);

        Member member = Member.create("Test email", "Test nickname", "Test profile image");
        memberRepository.save(member);

        // expected
        assertThatThrownBy(() -> teamMemberValidator.validateTeamHasMember(team.getId(), member.getId()))
            .isInstanceOf(EntityNotFoundException.class)
            .hasMessage(ErrorDetail.TEAM_MEMBER_NOT_FOUND.getMessage());
    }

    @DisplayName("팀에 해당 멤버가 존재하는 경우 예외가 발생하지 않는다")
    @Test
    void validateTeamHasMember() {
        // given
        Team team = Team.create("Test Team", "Test Description", "Test Organization");
        teamRepository.save(team);

        Member member = Member.create("Test email", "Test nickname", "Test profile image");
        memberRepository.save(member);

        TeamMember teamMember = TeamMember.createMember(member, team, Position.NONE);
        teamMemberRepository.save(teamMember);

        // expected
        assertThatCode(() -> teamMemberValidator.validateTeamHasMember(team.getId(), member.getId()))
            .doesNotThrowAnyException();
    }

}