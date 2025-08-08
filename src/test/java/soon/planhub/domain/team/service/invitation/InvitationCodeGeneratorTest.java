package soon.planhub.domain.team.service.invitation;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import soon.planhub.IntegrationTestSupport;
import soon.planhub.domain.team.entity.InvitationCode;
import soon.planhub.domain.team.entity.Team;
import soon.planhub.domain.team.repository.TeamRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.*;

class InvitationCodeGeneratorTest extends IntegrationTestSupport {

    @Autowired
    private InvitationCodeGenerator invitationCodeGenerator;

    @Autowired
    private TeamRepository teamRepository;

    @MockitoBean
    private CodeGenerator codeGenerator;

    @AfterEach
    void tearDown() {
        teamRepository.deleteAllInBatch();
    }

    @DisplayName("초대코드가 존재하지 않는 경우 새로 생성한다.")
    @Test
    void generateCodeWhenCodeIsNull() {
        // given
        Team team = Team.create("Test name", "Test description", "Test organization");
        teamRepository.save(team);

        given(codeGenerator.generate())
            .willReturn("newCode1");

        // when
        String code = invitationCodeGenerator.generateInvitationCode(team.getId());

        // then
        assertThat(code).isNotNull()
            .hasSize(8)
            .isEqualTo("newCode1");

        Team foundTeam = teamRepository.findById(team.getId());
        assertThat(foundTeam.getInvitationCode().getCode())
            .isNotNull()
            .isEqualTo(code);
    }

    @DisplayName("초대 코드가 만료되어 있는 경우 새로 생성한다.")
    @Test
    void generateCodeWhenCodeIsExpired() {
        // given
        Team team = Team.create("Test name", "Test description", "Test organization");
        LocalDateTime expiredTime = LocalDateTime.now().minusDays(1);
        InvitationCode invitationCode = InvitationCode.createWithCode("expired1", expiredTime);
        team.refreshInvitationCode(invitationCode);
        teamRepository.save(team);

        String oldCode = team.getInvitationCode().getCode();
        given(codeGenerator.generate())
            .willReturn("newCode1");

        // when
        String newCode = invitationCodeGenerator.generateInvitationCode(
            team.getId());

        // then
        assertThat(newCode).isNotNull()
            .hasSize(8)
            .isNotEqualTo(oldCode)
            .isEqualTo("newCode1");
    }

    @DisplayName("유효한 초대코드가 존재한다면, 기존 코드를 반환한다.")
    @Test
    void generateCodeWhenCodeIsValid() {
        // given
        Team team = Team.create("Test name", "Test description", "Test organization");
        InvitationCode invitationCode = InvitationCode.createWithCode("valid123", LocalDateTime.now());
        team.refreshInvitationCode(invitationCode);
        teamRepository.save(team);

        String oldCode = team.getInvitationCode().getCode();

        // when
        String result = invitationCodeGenerator.generateInvitationCode(team.getId());

        // then
        assertThat(result).isNotNull()
            .hasSize(8)
            .isEqualTo(oldCode);
        verify(codeGenerator, never()).generate();
    }

    @DisplayName("코드 중복 생성 시 재시도 로직이 동작한다.")
    @Test
    void generateRetriesWhenCodeExists() {
        // given
        Team team = Team.create("Test name", "Test description", "Test organization");
        teamRepository.save(team);

        Team anotherTeam = Team.create("Another name", "Another description", "Another organization");
        InvitationCode existingCode = InvitationCode.createWithCode("existing", LocalDateTime.now());
        anotherTeam.refreshInvitationCode(existingCode);
        teamRepository.save(anotherTeam);

        given(codeGenerator.generate())
            .willReturn("existing", "newCode1");

        // when
        String resultCode = invitationCodeGenerator.generateInvitationCode(team.getId());

        // then
        assertThat(resultCode).isNotNull()
            .hasSize(8)
            .isEqualTo("newCode1");
        verify(codeGenerator, times(2)).generate();
    }

    @DisplayName("생성된 초대 코드는 8자리 알파벳+숫자로 구성된다.")
    @Test
    void generatedCodeFormatIsValid() {
        // given
        Team team = Team.create("Test", "desc", "org");
        teamRepository.save(team);

        given(codeGenerator.generate())
            .willReturn("A1B2C3D4");

        // when
        String code = invitationCodeGenerator.generateInvitationCode(team.getId());

        // then
        assertThat(code)
            .matches("[A-Za-z0-9]{8}");
    }

    @DisplayName("최대 재시도 횟수 초과 시 예외가 발생한다.")
    @Test
    void generateCodeThrowExceptionWhenMaxRetryExceeded() {
        // given
        Team team = Team.create("Test name", "Test description", "Test organization");
        teamRepository.save(team);

        Team anotherTeam = Team.create("Another name", "Another description", "Another organization");
        InvitationCode existingCode = InvitationCode.createWithCode("existing", LocalDateTime.now());
        anotherTeam.refreshInvitationCode(existingCode);
        teamRepository.save(anotherTeam);

        given(codeGenerator.generate())
            .willReturn("existing");

        // expected
        assertThatThrownBy(() -> invitationCodeGenerator.generateInvitationCode(team.getId()))
            .isInstanceOf(IllegalStateException.class)
            .hasMessage("초대 코드 생성에 실패했습니다. (최대 재시도 횟수 초과)");

        verify(codeGenerator, times(5)).generate();
    }

    @DisplayName("여러 스레드가 같은 팀의 코드 생성을 요청해도 하나의 코드만 생성한다.")
    @Test
    void concurrentGenerate() throws InterruptedException {
        // given
        Team team = Team.create("Test name", "Test description", "Test organization");
        teamRepository.save(team);

        given(codeGenerator.generate())
            .willReturn("newCode");

        int threadCount = 10;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        Set<String> resultCodes = Collections.synchronizedSet(new HashSet<>());

        // when
        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                try {
                    String code = invitationCodeGenerator.generateInvitationCode(
                        team.getId());
                    resultCodes.add(code);
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();

        // then : 비관적 락을 사용하여 동시성 문제를 방지하므로, 결과 코드의 크기는 1이어야 한다.
        assertThat(resultCodes).hasSize(1);
        verify(codeGenerator, times(1)).generate();
    }

}