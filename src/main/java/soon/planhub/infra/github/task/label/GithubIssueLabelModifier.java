package soon.planhub.infra.github.task.label;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import soon.planhub.global.exception.common.GithubApiException;
import soon.planhub.global.exception.dto.ErrorDetail;
import soon.planhub.infra.client.RestClientProvider;
import soon.planhub.infra.github.dto.GithubIssueLabelUpdateRequest;

@Slf4j
@RequiredArgsConstructor
@Component
public class GithubIssueLabelModifier {

    private static final String ISSUE_LABEL_URL = "/repos/{organizationName}/{repositoryName}/labels";

    private final RestClientProvider restClientProvider;

    @Async
    @Retryable(
        retryFor = {GithubApiException.class},
        maxAttempts = 5,
        backoff = @Backoff(delay = 1000, multiplier = 1.5)
    )
    public void updateGithubIssueLabelAsync(GithubIssueLabelUpdateRequest request) {
        try {
            RestClient restClient = restClientProvider.createClient(request.token());
            restClient.patch()
                .uri(ISSUE_LABEL_URL + "/{labelName}", request.organizationName(), request.repositoryName(), request.oldTitle())
                .body(request.toBody())
                .retrieve()
                .body(Void.class);
        } catch (Exception e) {
            log.error("GitHub 라벨 '{}' 업데이트 중 에러 발생: {}", request.oldTitle(), e.getMessage(), e);
            throw new GithubApiException(ErrorDetail.GITHUB_API_ERROR, e);
        }
    }

    @Recover
    public void recover(GithubApiException e, GithubIssueLabelUpdateRequest request) {
        log.error("GitHub 라벨 '{}' 업데이트 재시도 실패. 요청 정보: {}", request.oldTitle(), request, e);
        // TODO: 재시도 실패 시 알림 전송 + 저장된 데이터 롤백 처리
    }

}