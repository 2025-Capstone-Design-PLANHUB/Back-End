package soon.planhub.domain.team.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import soon.planhub.domain.team.port.out.TeamAuthPort;
import soon.planhub.global.exception.dto.ErrorDetail;
import soon.planhub.global.exception.team.IsNotAdminInOrganizationException;

@RequiredArgsConstructor
@Component
public class TeamValidator {

    private final TeamAuthPort teamAuthPort;

    public void validateAdminPermission(String organizationName, Long creatorId) {
        boolean isAdmin = teamAuthPort.isActiveAdmin(organizationName, creatorId);
        if (!isAdmin) {
            throw new IsNotAdminInOrganizationException(ErrorDetail.IS_NOT_ADMIN_IN_ORGANIZATION);
        }
    }

}
