package soon.planhub.infra.client;

import org.springframework.web.client.RestClient;

public interface RestClientProvider {

    RestClient createClient(String token);

}