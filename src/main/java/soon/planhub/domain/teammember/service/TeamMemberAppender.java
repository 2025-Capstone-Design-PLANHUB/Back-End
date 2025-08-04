package soon.planhub.domain.teammember.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import soon.planhub.domain.member.entity.Member;
import soon.planhub.domain.team.entity.Team;
import soon.planhub.domain.teammember.entity.TeamMember;
import soon.planhub.domain.teammember.repository.TeamMemberRepository;

@RequiredArgsConstructor
@Component
public class TeamMemberAppender {

    private final TeamMemberRepository teamMemberRepository;
    private final TeamMemberValidator teamMemberValidator;

    @Transactional
    public void appendToLeader(Member member, Team team) {
        teamMemberValidator.validateTeamHasNoLeader(team.getId());

        TeamMember teamMember = TeamMember.createLeader(member, team);
        teamMemberRepository.save(teamMember);
    }

}