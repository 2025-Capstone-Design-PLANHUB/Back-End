package soon.planhub.domain.team.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import soon.planhub.domain.member.entity.Member;
import soon.planhub.domain.member.repository.MemberRepository;
import soon.planhub.domain.team.entity.Team;
import soon.planhub.domain.team.repository.TeamRepository;
import soon.planhub.domain.team.service.dto.TeamInformation;
import soon.planhub.domain.teammember.service.TeamMemberAppender;

@RequiredArgsConstructor
@Component
public class TeamCreator {

    private final TeamRepository teamRepository;
    private final MemberRepository memberRepository;
    private final TeamMemberAppender teamMemberAppender;

    @Transactional
    public Long createTeam(TeamInformation info, Long creatorId) {
        Team team = Team.create(
            info.name(),
            info.description(),
            info.organizationName()
        );
        teamRepository.save(team);

        Member creator = memberRepository.findById(creatorId);
        teamMemberAppender.appendToLeader(creator, team);

        return team.getId();
    }

}