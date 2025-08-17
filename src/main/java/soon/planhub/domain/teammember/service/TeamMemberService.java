package soon.planhub.domain.teammember.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import soon.planhub.domain.team.service.TeamValidator;
import soon.planhub.domain.teammember.port.out.TeamMemberPort;
import soon.planhub.domain.teammember.service.dto.request.TeamMemberAppendServiceRequest;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class TeamMemberService {

    private final TeamMemberAppender teamMemberAppender;
    private final TeamMemberPort teamMemberPort;
    private final TeamValidator teamValidator;

    public Long append(TeamMemberAppendServiceRequest request, Long memberId) {
        LocalDateTime now = LocalDateTime.now();
        teamValidator.validateInvitationCode(request.invitationCode(), now); // TODO: TeamValidator가 여기에 존재하는 것이 맞는지 검토 필요

        teamMemberPort.appendMemberToOrg(request.teamId(), memberId); // 깃 허브에 요청
        return teamMemberAppender.appendToMember(memberId, request.teamId(), request.position());
    }

}