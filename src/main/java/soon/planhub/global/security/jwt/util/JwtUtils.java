package soon.planhub.global.security.jwt.util;

import static soon.planhub.global.security.jwt.common.TokenType.BEARER_PREFIX;

import java.util.Optional;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class JwtUtils {

    public static Optional<String> getTokenFromHeader(String headerValue) {
        if (StringUtils.hasText(headerValue) && headerValue.startsWith(BEARER_PREFIX.getValue())) {
            return Optional.of(headerValue.substring(BEARER_PREFIX.getValue().length()));
        }
        return Optional.empty();
    }

}