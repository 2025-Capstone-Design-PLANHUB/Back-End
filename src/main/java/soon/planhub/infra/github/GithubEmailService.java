package soon.planhub.infra.github;

import static soon.planhub.global.exception.dto.ErrorDetail.GITHUB_MEMBER_EMAIL_NOT_FOUND;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import soon.planhub.global.exception.member.GithubEmailNotFoundException;
import soon.planhub.infra.client.RestClientProvider;
import soon.planhub.infra.github.dto.GithubEmailDto;

@RequiredArgsConstructor
@Service
public class GithubEmailService {

    private static final String EMAILS_URL = "/user/emails";
    private final RestClientProvider restClientProvider;

    public String fetchPrimaryVerifiedEmail(String oauth2Token) {
        RestClient client = restClientProvider.createClient(oauth2Token);
        List<GithubEmailDto> emails = fetchEmails(client);
        return extractPrimaryVerifiedEmail(emails);
    }

    private List<GithubEmailDto> fetchEmails(RestClient client) {
        return Optional.ofNullable(client.get()
            .uri(EMAILS_URL)
            .retrieve()
            .body(new ParameterizedTypeReference<List<GithubEmailDto>>() {
            })
        ).orElseThrow(() -> new GithubEmailNotFoundException(GITHUB_MEMBER_EMAIL_NOT_FOUND));
    }

    private String extractPrimaryVerifiedEmail(List<GithubEmailDto> emails) {
        return emails.stream()
            .filter(email -> email.primary() && email.verified())
            .map(GithubEmailDto::email)
            .findFirst()
            .orElseThrow(
                () -> new GithubEmailNotFoundException(GITHUB_MEMBER_EMAIL_NOT_FOUND)
            );
    }

}