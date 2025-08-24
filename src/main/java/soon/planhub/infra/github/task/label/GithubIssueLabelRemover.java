package soon.planhub.infra.github.task.label;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import soon.planhub.global.exception.common.GithubApiException;
import soon.planhub.global.exception.dto.ErrorDetail;
import soon.planhub.infra.client.RestClientProvider;
import soon.planhub.infra.github.dto.GithubIssueLabelDeleteRequest;

@Slf4j
@RequiredArgsConstructor
@Component
public class GithubIssueLabelRemover {

    private static final String ISSUE_LABEL_URL = "/repos/{organizationName}/{repositoryName}/labels";

    private final RestClientProvider restClientProvider;

    @Async
    @Retryable(
        retryFor = {GithubApiException.class},
        maxAttempts = 5,
        backoff = @Backoff(delay = 1000, multiplier = 1.5)
    )
    public void deleteGithubIssueLabelAsync(GithubIssueLabelDeleteRequest request) {
        try {
            RestClient restClient = restClientProvider.createClient(request.token());
            restClient.delete()
                .uri(ISSUE_LABEL_URL + "/{labelName}", request.organizationName(), request.repositoryName(), request.title())
                .retrieve()
                .body(Void.class);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                log.warn("GitHub 라벨 '{}' 삭제 시도 중 해당 라벨을 찾을 수 없음: {}", request.title(), e.getMessage());
            } else {
                log.error("GitHub 라벨 '{}' 삭제 중 에러 발생: {}", request.title(), e.getMessage(), e);
                throw new GithubApiException(ErrorDetail.GITHUB_API_ERROR, e);
            }
        }
    }

    @Recover
    public void recover(GithubApiException e, GithubIssueLabelDeleteRequest request) {
        log.error("GitHub 라벨 '{}' 삭제 재시도 실패. 요청 정보: {}", request.title(), request, e);
        // TODO: GitHub API 최종 실패 시, DB의 데이터와 GitHub의 상태를 맞추기 위한 로직 구현
    }

}