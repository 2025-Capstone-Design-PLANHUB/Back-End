package soon.planhub.domain.team.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import soon.planhub.domain.team.entity.Team;
import soon.planhub.domain.team.port.out.TeamAuthPort;
import soon.planhub.domain.team.repository.TeamRepository;
import soon.planhub.global.exception.common.InvalidRequest;
import soon.planhub.global.exception.dto.ErrorDetail;
import soon.planhub.global.exception.team.IsNotAdminInOrganizationException;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Component
public class TeamValidator {

    private final TeamAuthPort teamAuthPort;
    private final TeamRepository teamRepository;

    public void validateAdminPermission(String organizationName, Long creatorId) {
        boolean isAdmin = teamAuthPort.isActiveAdmin(organizationName, creatorId);
        if (!isAdmin) {
            throw new IsNotAdminInOrganizationException(ErrorDetail.IS_NOT_ADMIN_IN_ORGANIZATION);
        }
    }

    public void validateInvitationCode(String code, LocalDateTime now) {
        Team team = teamRepository.findTeamByValidInvitationCode(code, now);
        if (team.shouldRefreshInvitationCode(now)) {
            throw new InvalidRequest("invitationCode", ErrorDetail.INVALID_INVITATION_CODE.getMessage());
        }
    }

}
