package soon.planhub.domain.member.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "members")
@Entity
public class Member extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(nullable = false)
    private String profileImageURL;

    @Column(unique = true)
    private String refreshToken;

    @Column(unique = true)
    private String oauthToken;

    public static Member create(String email, String nickname, String profileImageURL) {
        return Member.builder()
            .email(email)
            .nickname(nickname)
            .profileImageURL(profileImageURL)
            .role(Role.ROLE_USER)
            .build();
    }

    public static Member createOAuthMember(
        String email,
        String nickname,
        String profileImageURL,
        String oauthToken
    ) {
        return Member.builder()
            .email(email)
            .nickname(nickname)
            .profileImageURL(profileImageURL)
            .role(Role.ROLE_USER)
            .oauthToken(oauthToken)
            .build();
    }

    public void updateProfile(String nickname, String profileImageURL) {
        this.nickname = nickname;
        this.profileImageURL = profileImageURL;
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void updateOauthToken(String oauthToken) {
        this.oauthToken = oauthToken;
    }

}