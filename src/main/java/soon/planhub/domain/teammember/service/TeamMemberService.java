package soon.planhub.domain.teammember.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import soon.planhub.domain.teammember.repository.TeamMemberRepository;

@RequiredArgsConstructor
@Service
public class TeamMemberService {

    private final TeamMemberRepository teamMemberRepository;

}