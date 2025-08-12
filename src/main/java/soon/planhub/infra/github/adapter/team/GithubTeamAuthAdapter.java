package soon.planhub.infra.github.adapter.team;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import soon.planhub.domain.member.repository.MemberRepository;
import soon.planhub.domain.team.port.out.TeamAuthPort;
import soon.planhub.infra.github.organization.service.GithubOrganizationReader;

@Slf4j
@RequiredArgsConstructor
@Component
public class GithubTeamAuthAdapter implements TeamAuthPort {

    private final MemberRepository memberRepository;
    private final GithubOrganizationReader githubOrganizationReader;

    @Override
    public boolean isActiveAdmin(String organizationName, Long memberId) {
        String oauthToken = memberRepository.findOauthTokenById(memberId);

        log.info("org {}의 멤버 {}가 관리자 권한을 가지고 있는지 확인", organizationName, memberId);
        return githubOrganizationReader.isActiveAdminInOrg(oauthToken, organizationName);
    }

}