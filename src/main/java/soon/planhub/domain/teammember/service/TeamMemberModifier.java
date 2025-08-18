package soon.planhub.domain.teammember.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import soon.planhub.domain.teammember.entity.TeamMember;
import soon.planhub.domain.teammember.repository.TeamMemberRepository;

@RequiredArgsConstructor
@Component
public class TeamMemberModifier {

    private final TeamMemberRepository teamMemberRepository;

    @Transactional
    public void updatePosition(Long teamMemberId, String position) {
        TeamMember teamMember = teamMemberRepository.findById(teamMemberId);
        teamMember.updatePosition(position);
    }

    @Transactional
    public void updateVisibility(Long memberId, Long teamId, boolean isVisible) {
        TeamMember teamMember = teamMemberRepository.findByTeamIdAndMemberId(teamId, memberId);
        teamMember.updateVisibility(isVisible);
    }

}