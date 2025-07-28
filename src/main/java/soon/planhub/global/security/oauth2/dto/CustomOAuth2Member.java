package soon.planhub.global.security.oauth2.dto;

import java.util.Collection;
import java.util.Map;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

@Getter
public class CustomOAuth2Member extends DefaultOAuth2User {

    private final Long memberId;
    private final String nickname;
    private final String oauthAccessToken;

    public CustomOAuth2Member(
        Collection<? extends GrantedAuthority> authorities,
        Map<String, Object> attributes,
        String nameAttributeKey,
        Long memberId,
        String nickname,
        String oauthAccessToken
    ) {
        super(authorities, attributes, nameAttributeKey);
        this.memberId = memberId;
        this.nickname = nickname;
        this.oauthAccessToken = oauthAccessToken;
    }

}