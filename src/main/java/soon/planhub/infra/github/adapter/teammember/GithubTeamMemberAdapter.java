package soon.planhub.infra.github.adapter.teammember;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import soon.planhub.domain.member.entity.Member;
import soon.planhub.domain.member.repository.MemberRepository;
import soon.planhub.domain.team.entity.Team;
import soon.planhub.domain.team.repository.TeamRepository;
import soon.planhub.domain.teammember.port.out.TeamMemberPort;
import soon.planhub.domain.teammember.service.TeamMemberReader;
import soon.planhub.infra.github.organization.GithubOrganizationAppender;

@RequiredArgsConstructor
@Component
public class GithubTeamMemberAdapter implements TeamMemberPort {

    private final TeamRepository teamRepository;
    private final MemberRepository memberRepository;
    private final TeamMemberReader teamMemberReader;
    private final GithubOrganizationAppender githubOrganizationAppender;

    @Override
    public void appendMemberToOrg(Long teamId, Long memberId) {
        Team team = teamRepository.findById(teamId);
        Member member = memberRepository.findById(memberId);
        String oauthToken = teamMemberReader.findLeaderOauthTokenByTeamId(teamId);
        githubOrganizationAppender.appendMemberToOrg(team.getOrganizationName(), member.getNickname(), oauthToken);
    }

}