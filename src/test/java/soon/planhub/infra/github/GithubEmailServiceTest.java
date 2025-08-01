package soon.planhub.infra.github;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static soon.planhub.global.exception.dto.ErrorDetail.GITHUB_MEMBER_EMAIL_NOT_FOUND;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;
import soon.planhub.global.exception.member.GithubEmailNotFoundException;
import soon.planhub.infra.client.RestClientProvider;
import soon.planhub.infra.client.github.GithubRestClientProvider;
import soon.planhub.infra.github.dto.GithubEmailDto;

class GithubEmailServiceTest {

    private RestClient restClient;
    private RestClientProvider restClientProvider;
    private GithubEmailService githubEmailService;

    @BeforeEach
    void setUp() {
        restClientProvider = mock(GithubRestClientProvider.class);
        restClient = mock(RestClient.class, RETURNS_DEEP_STUBS);
        githubEmailService = new GithubEmailService(restClientProvider);
    }

    @DisplayName("primary이며 verified된 이메일을 반환한다.")
    @Test
    void fetchPrimaryVerifiedEmailReturnsPrimaryVerifiedEmail() {
        // given
        String mockedToken = "mockedToken";
        List<GithubEmailDto> emails = List.of(
            new GithubEmailDto("a@a.com", false, true),
            new GithubEmailDto("b@b.com", true, true)
        );

        given(restClientProvider.createClient(Mockito.any()))
            .willReturn(restClient);
        given(restClient.get()
            .uri("/user/emails")
            .retrieve()
            .body(any(ParameterizedTypeReference.class))
        ).willReturn(emails);

        // when
        String result = githubEmailService.fetchPrimaryVerifiedEmail(mockedToken);

        // then
        assertThat(result).isEqualTo("b@b.com");
    }

    @DisplayName("primary이며 verified된 이메일이 없는 경우 예외가 발생한다.")
    @Test
    void fetchPrimaryVerifiedEmailThrowsIfNotFound() {
        // given
        String mockedToken = "mockedToken";
        List<GithubEmailDto> emails = List.of(
            new GithubEmailDto("a@a.com", false, true),
            new GithubEmailDto("b@b.com", true, false)
        );

        given(restClientProvider.createClient(Mockito.any()))
            .willReturn(restClient);
        given(restClient.get()
            .uri("/user/emails")
            .retrieve()
            .body(any(ParameterizedTypeReference.class))
        ).willReturn(emails);

        // expected
        assertThatThrownBy(() -> githubEmailService.fetchPrimaryVerifiedEmail(mockedToken))
            .isInstanceOf(GithubEmailNotFoundException.class)
            .hasMessage(GITHUB_MEMBER_EMAIL_NOT_FOUND.getMessage());
    }

}