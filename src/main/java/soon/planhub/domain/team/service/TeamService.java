package soon.planhub.domain.team.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import soon.planhub.domain.team.service.dto.request.TeamCreateServiceRequest;

@RequiredArgsConstructor
@Service
public class TeamService {

    private final TeamCreator teamCreator;

    public Long createTeam(TeamCreateServiceRequest request, Long creatorId) {
        return teamCreator.createTeam(request.toInfo(), creatorId);
    }

}