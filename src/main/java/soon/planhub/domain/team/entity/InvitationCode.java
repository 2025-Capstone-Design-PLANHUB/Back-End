package soon.planhub.domain.team.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class InvitationCode {

    @Column(name = "invitation_code", unique = true, length = 8)
    private String code;

    private LocalDateTime expirationTime;

    public static InvitationCode createWithCode(String invitationCode) {
        LocalDateTime expirationTime = LocalDateTime.now().plusDays(1L);
        return new InvitationCode(invitationCode, expirationTime);
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expirationTime);
    }

}