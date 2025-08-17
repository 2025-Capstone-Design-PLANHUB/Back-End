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
import soon.planhub.domain.teammember.entity.TeamMember;
import soon.planhub.domain.teammember.repository.TeamMemberRepository;

import static org.assertj.core.api.Assertions.assertThat;

class TeamMemberReaderTest extends IntegrationTestSupport {

    @Autowired
    private TeamMemberReader teamMemberReader;

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

}