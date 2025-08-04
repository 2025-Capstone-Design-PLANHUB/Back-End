package soon.planhub.domain.team.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import soon.planhub.IntegrationTestSupport;
import soon.planhub.domain.member.entity.Member;
import soon.planhub.domain.member.repository.MemberRepository;
import soon.planhub.domain.team.entity.Team;
import soon.planhub.domain.team.repository.TeamRepository;
import soon.planhub.domain.team.service.dto.TeamInformation;
import soon.planhub.domain.teammember.entity.Role;
import soon.planhub.domain.teammember.entity.TeamMember;
import soon.planhub.domain.teammember.repository.TeamMemberRepository;

class TeamCreatorTest extends IntegrationTestSupport {

    @Autowired
    private TeamCreator teamCreator;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private TeamMemberRepository teamMemberRepository;

    @AfterEach
    void tearDown() {
        teamMemberRepository.deleteAllInBatch();
        teamRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
    }

    @DisplayName("팀을 생성하고 생성자를 리더로 설정한다.")
    @Test
    void createTeamWithCreatorAsLeader() {
        // given
        Member member = Member.create("Test email", "Test nickname", "Test profile image");
        memberRepository.save(member);

        TeamInformation info = TeamInformation.builder()
            .name("Test Team")
            .description("Test Description")
            .organizationName("Test Organization")
            .build();

        // when
        Long teamId = teamCreator.createTeam(info, member.getId());

        // then
        Team team = teamRepository.findById(teamId);
        assertThat(team).isNotNull();

        TeamMember teamMember = teamMemberRepository.findByTeamId(team.getId());
        assertThat(teamMember.getRole()).isEqualTo(Role.ROLE_LEADER);
    }

}