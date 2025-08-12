package soon.planhub.infra.github.organization.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import soon.planhub.infra.client.RestClientProvider;
import soon.planhub.infra.github.organization.service.dto.GithubOrgMembership;

@Slf4j
@RequiredArgsConstructor
@Component
public class GithubOrganizationReader {

    private static final String MEMBERSHIP_URL = "/user/memberships/orgs/{organizationName}";

    private final RestClientProvider restClientProvider;

    public boolean isActiveAdminInOrg(String oauth2Token, String organizationName) {
        RestClient client = restClientProvider.createClient(oauth2Token);

        try {
            GithubOrgMembership body = client.get()
                .uri(MEMBERSHIP_URL, organizationName)
                .retrieve()
                .body(GithubOrgMembership.class);
            return body != null && body.isActiveAdmin();
        } catch (HttpClientErrorException e) {
            log.error("오가니제이션 '{}' 멤버십 조회 실패: {}", organizationName, e.getMessage(), e);
            return false;
        }

    }

}