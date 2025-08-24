package soon.planhub.global.annotation;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import soon.planhub.domain.teammember.service.TeamMemberValidator;

@RequiredArgsConstructor
@Aspect
@Component
public class TeamMembershipAspect {

    private final TeamMemberValidator teamMemberValidator;

    @Around(value = "@annotation(soon.planhub.global.annotation.TeamMembership) && args(teamId, memberId, ..)", argNames = "joinPoint,teamId,memberId")
    public Object validateTeamMembership(ProceedingJoinPoint joinPoint, Long teamId, Long memberId) throws Throwable {
        teamMemberValidator.validateTeamHasMember(teamId, memberId);
        return joinPoint.proceed();
    }

}