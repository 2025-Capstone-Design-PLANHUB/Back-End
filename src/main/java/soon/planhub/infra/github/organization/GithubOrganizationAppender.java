package soon.planhub.infra.github.organization;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import soon.planhub.global.exception.dto.ErrorDetail;
import soon.planhub.global.exception.member.OAuthTokenExpiredException;
import soon.planhub.infra.client.RestClientProvider;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Slf4j
@RequiredArgsConstructor
@Component
public class GithubOrganizationAppender {

    private static final String ORGANIZATION_MEMBER_URL = "/orgs/{organizationName}/memberships/{username}";

    private final RestClientProvider restClientProvider;

    @Async
    public void appendMemberToOrgAsync(String orgName, String nickname, String oauthToken) {
        RestClient client = restClientProvider.createClient(oauthToken);

        try {
            client.put()
                .uri(ORGANIZATION_MEMBER_URL, orgName, nickname)
                .retrieve()
                .body(Void.class);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().value() == UNAUTHORIZED.value()) {
                throw new OAuthTokenExpiredException(ErrorDetail.OAUTH_TOKEN_EXPIRED);
            }
            log.error("organization 멤버 추가 중 에러 발생 member: {}, organization: {}", nickname, orgName, e);
        }
    }

}