package soon.planhub.domain.teammember.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class TeamMemberRepository {

    private final TeamMemberJpaRepository teamMemberJpaRepository;

}