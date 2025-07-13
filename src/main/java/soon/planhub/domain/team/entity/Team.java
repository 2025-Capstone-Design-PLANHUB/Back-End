package soon.planhub.domain.team.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import soon.planhub.domain.BaseEntity;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "teams")
@Entity
public class Team extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false, unique = true)
    private String organizationName;

    @Embedded
    private InvitationCode invitationCode;

    public static Team create(String name, String description, String organizationName) {
        return Team.builder()
            .name(name)
            .description(description)
            .organizationName(organizationName)
            .invitationCode(null) // 초기에는 코드가 없으므로 null로 설정
            .build();
    }

    public void refreshInvitationCode(InvitationCode invitationCode) {
        this.invitationCode = invitationCode;
    }

}