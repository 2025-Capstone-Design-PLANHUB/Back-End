package soon.planhub.domain.member.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public record Token(

    @Column(nullable = false, unique = true)
    String refreshToken,

    @Column(nullable = false, unique = true)
    String oauthToken

) {

}