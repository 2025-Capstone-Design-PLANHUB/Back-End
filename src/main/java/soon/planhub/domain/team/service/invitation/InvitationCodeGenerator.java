package soon.planhub.domain.team.service.invitation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import soon.planhub.domain.team.entity.InvitationCode;
import soon.planhub.domain.team.entity.Team;
import soon.planhub.domain.team.repository.TeamRepository;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Component
public class InvitationCodeGenerator {

    private static final int MAX_RETRY_COUNT = 5;

    private final TeamRepository teamRepository;
    private final CodeGenerator codeGenerator;

    @Transactional
    public String generateInvitationCode(Long teamId) {
        Team team = teamRepository.findByIdWithPessimisticLock(teamId);
        LocalDateTime now = LocalDateTime.now();

        if (team.shouldRefreshInvitationCode(now)) {
            String code = generateUniqueCode(now);
            InvitationCode newInvitationCode = InvitationCode.createWithCode(code, now);
            team.refreshInvitationCode(newInvitationCode);
        }

        return team.getInvitationCode().getCode();
    }

    private String generateUniqueCode(LocalDateTime now) {
        for (int i = 0; i < MAX_RETRY_COUNT; i++) {
            String code = codeGenerator.generate();
            if (!teamRepository.existsByInvitationCode(code, now)) {
                return code;
            }
        }

        throw new IllegalStateException("초대 코드 생성에 실패했습니다. (최대 재시도 횟수 초과)");
    }

}