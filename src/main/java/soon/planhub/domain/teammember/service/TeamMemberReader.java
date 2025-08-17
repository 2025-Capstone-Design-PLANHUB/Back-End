package soon.planhub.domain.teammember.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import soon.planhub.domain.teammember.repository.TeamMemberRepository;
import soon.planhub.domain.teammember.service.dto.response.TeamMemberDetailResponse;

import java.util.List;

@RequiredArgsConstructor
@Component
public class TeamMemberReader {

    private final TeamMemberRepository teamMemberRepository;

    public String findLeaderOauthTokenByTeamId(Long teamId) {
        return teamMemberRepository.findLeaderByTeamId(teamId)
            .getMember()
            .getOauthToken();
    }

    @Transactional(readOnly = true)
    public List<TeamMemberDetailResponse> getTeamMembers(Long teamId) {
        return teamMemberRepository.findAllByTeamId(teamId)
            .stream()
            .map(TeamMemberDetailResponse::from)
            .toList();
    }

}