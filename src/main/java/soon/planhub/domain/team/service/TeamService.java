package soon.planhub.domain.team.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import soon.planhub.domain.team.repository.TeamRepository;

@RequiredArgsConstructor
@Service
public class TeamService {

    private final TeamRepository teamRepository;

}