package soon.planhub.global.security.jwt.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TokenType {

    AUTHORIZATION_HEADER("Authorization"),
    BEARER_PREFIX("Bearer ");

    private final String value;

}