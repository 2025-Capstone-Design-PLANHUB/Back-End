package soon.planhub.infra.client.github;

import static soon.planhub.global.security.jwt.common.TokenType.AUTHORIZATION_HEADER;
import static soon.planhub.global.security.jwt.common.TokenType.BEARER_PREFIX;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import soon.planhub.infra.client.RestClientProvider;

@Primary
@Component
public class GithubRestClientProvider implements RestClientProvider {

    private final RestClient baseClient;

    public GithubRestClientProvider(@Value("${api.github.base-url}") String baseUrl) {
        this.baseClient = RestClient.builder()
            .baseUrl(baseUrl)
            .defaultHeader("Accept", "application/vnd.github+json")
            .build();
    }

    @Override
    public RestClient createClient(String oauth2Token) {
        return baseClient.mutate()
            .defaultHeader(AUTHORIZATION_HEADER.getValue(), BEARER_PREFIX.getValue() + oauth2Token)
            .build();
    }

}