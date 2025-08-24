package soon.planhub.infra.github.task.label;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import soon.planhub.domain.task.service.dto.label.response.TaskLabelDetailResponse;
import soon.planhub.infra.client.RestClientProvider;
import soon.planhub.infra.github.dto.GithubIssueLabelDetailResponse;
import soon.planhub.infra.github.dto.GithubIssueLabelGetRequest;

import java.util.Collections;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class GithubIssueLabelReader {

    private static final String ISSUE_LABEL_URL = "/repos/{organizationName}/{repositoryName}/labels";

    private final RestClientProvider restClientProvider;

    public List<TaskLabelDetailResponse> getGithubIssueLabels(GithubIssueLabelGetRequest request) {
        try {
            RestClient restClient = restClientProvider.createClient(request.token());
            List<GithubIssueLabelDetailResponse> labels = restClient.get()
                .uri(ISSUE_LABEL_URL, request.organizationName(), request.repositoryName())
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });

            return labels == null ? Collections.emptyList() :
                labels.stream()
                    .map(TaskLabelDetailResponse::of)
                    .toList();
        } catch (HttpClientErrorException e) {
            log.error("GitHub API 호출 중 오류 발생: {}", e.getMessage(), e);
            return Collections.emptyList();
        } catch (Exception e) {
            log.error("이슈 라벨 조회 중 에러 발생", e);
            return Collections.emptyList();
        }
    }

}