package soon.planhub.domain.teammember.service;

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
import soon.planhub.domain.teammember.service.dto.response.TeamMemberDetailResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

class TeamMemberReaderTest extends IntegrationTestSupport {

    @Autowired
    private TeamMemberReader teamMemberReader;

    @Autowired
    private TeamMemberRepository teamMemberRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TeamRepository teamRepository;

    @DisplayName("리더의 Oauth 토큰을 찾는다.")
    @Test
    void findLeaderOauthToken() {
        // given
        Member member = Member.createOAuthMember("email", "nickname", "imageURL", "leader-token");
        memberRepository.save(member);

        Team team = Team.create("name", "description", "organization");
        teamRepository.save(team);

        TeamMember leader = TeamMember.createLeader(member, team);
        teamMemberRepository.save(leader);

        // when
        String oauthToken = teamMemberReader.findLeaderOauthTokenByTeamId(team.getId());

        // then
        assertThat(oauthToken).isEqualTo("leader-token");
    }

    @DisplayName("팀의 모든 팀원을 조회한다.")
    @Test
    void getTeamMembers() {
        // given
        Member leader = Member.create("leaderEmail", "leader", "imageURL");
        Member backend = Member.create("backendEmail", "backend", "imageURL");
        Member designer = Member.create("designerEmail", "designer", "imageURL");
        memberRepository.saveAll(List.of(leader, backend, designer));

        Team team = Team.create("teamName", "teamDescription", "organization");
        teamRepository.save(team);

        TeamMember teamLeader = TeamMember.createLeader(leader, team);
        TeamMember teamBackend = TeamMember.createMember(backend, team, Position.BACKEND.name());
        TeamMember teamDesigner = TeamMember.createMember(designer, team, Position.DESIGNER.name());
        teamMemberRepository.saveAll(List.of(teamLeader, teamBackend, teamDesigner));

        // when
        List<TeamMemberDetailResponse> teamMembers = teamMemberReader.getTeamMembers(team.getId());

        // then
        assertThat(teamMembers).hasSize(3)
            .extracting("nickname", "role", "position")
            .containsExactlyInAnyOrder(
                tuple("leader", "ROLE_LEADER", "NONE"),
                tuple("backend", "ROLE_MEMBER", "BACKEND"),
                tuple("designer", "ROLE_MEMBER", "DESIGNER")
            );
    }

}