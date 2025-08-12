package soon.planhub.domain.teammember.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import soon.planhub.domain.member.entity.Member;
import soon.planhub.domain.member.repository.MemberRepository;
import soon.planhub.domain.team.entity.Team;
import soon.planhub.domain.team.repository.TeamRepository;
import soon.planhub.domain.teammember.entity.TeamMember;
import soon.planhub.domain.teammember.repository.TeamMemberRepository;

@RequiredArgsConstructor
@Component
public class TeamMemberAppender {

    private final TeamMemberRepository teamMemberRepository;
    private final MemberRepository memberRepository;
    private final TeamRepository teamRepository;
    private final TeamMemberValidator teamMemberValidator;

    @Transactional
    public void appendToLeader(Long memberId, Team team) {
        Member member = memberRepository.findById(memberId);

        teamMemberValidator.validateTeamHasNoLeader(team.getId());

        TeamMember teamMember = TeamMember.createLeader(member, team);
        teamMemberRepository.save(teamMember);
    }

    @Transactional
    public Long appendToMember(Long memberId, Long teamId, String position) {
        Member member = memberRepository.findById(memberId);
        Team team = teamRepository.findById(teamId);
        teamMemberValidator.validateTeamHasNoMember(team.getId(), member.getId());

        TeamMember teamMember = TeamMember.createMember(member, team, position);
        teamMemberRepository.save(teamMember);

        return team.getId();
    }

}