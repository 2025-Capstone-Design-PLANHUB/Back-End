package soon.planhub.domain.teammember.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import soon.planhub.domain.teammember.repository.TeamMemberRepository;

@RequiredArgsConstructor
@Component
public class TeamMemberReader {

    private final TeamMemberRepository teamMemberRepository;

    public String findLeaderOauthTokenByTeamId(Long teamId) {
        return teamMemberRepository.findLeaderByTeamId(teamId)
            .getMember()
            .getOauthToken();
    }

}