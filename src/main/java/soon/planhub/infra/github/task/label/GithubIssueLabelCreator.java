package soon.planhub.infra.github.task.label;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import soon.planhub.global.exception.common.GithubApiException;
import soon.planhub.global.exception.dto.ErrorDetail;
import soon.planhub.global.exception.task.label.AlreadyIssueLabelException;
import soon.planhub.infra.client.RestClientProvider;
import soon.planhub.infra.github.dto.GithubIssueLabelCreateRequest;

import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@Slf4j
@RequiredArgsConstructor
@Component
public class GithubIssueLabelCreator {

    private static final String ISSUE_LABEL_URL = "/repos/{organizationName}/{repositoryName}/labels";

    private final RestClientProvider restClientProvider;

    @Async
    @Retryable(
        retryFor = {GithubApiException.class},
        maxAttempts = 5,
        backoff = @Backoff(delay = 1000, multiplier = 1.5)
    )
    public void createGithubIssueLabelAsync(GithubIssueLabelCreateRequest request) {
        try {
            RestClient restClient = restClientProvider.createClient(request.token());

            restClient.post()
                .uri(ISSUE_LABEL_URL, request.organizationName(), request.repositoryName())
                .body(request.toBody())
                .retrieve()
                .body(Void.class);

        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == UNPROCESSABLE_ENTITY) {
                log.info("라벨 '{}' 이미 GitHub에 존재합니다.", request.name());
                throw new AlreadyIssueLabelException(ErrorDetail.TASK_LABEL_ALREADY_EXISTS);
            }

            log.error("GitHub API 호출 중 오류 발생: {}", e.getMessage(), e);
            throw new GithubApiException(ErrorDetail.GITHUB_API_ERROR, e);
        } catch (Exception e) {
            log.error("GitHub 라벨 '{}' 추가 중 에러 발생: {}", request.name(), e.getMessage(), e);
            throw new GithubApiException(ErrorDetail.GITHUB_API_ERROR, e);
        }
    }

    @Recover
    public void recover(GithubApiException e, GithubIssueLabelCreateRequest request) {
        log.error("GitHub 라벨 '{}' 생성 실패: 모든 재시도 소진", request.name(), e);
    }

}