package soon.planhub.domain.team.service.invitation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import soon.planhub.domain.team.repository.TeamRepository;

@RequiredArgsConstructor
@Component
public class InvitationReader {

    private final TeamRepository teamRepository;

    @Transactional(readOnly = true)
    public String findInvitationCodeByTeamId(Long teamId) {
        return teamRepository.findInvitationCodeByTeamId(teamId);
    }

}