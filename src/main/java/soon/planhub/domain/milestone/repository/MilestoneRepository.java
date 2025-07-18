package soon.planhub.domain.milestone.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class MilestoneRepository {

    private final MilestoneJpaRepository milestoneJpaRepository;

}