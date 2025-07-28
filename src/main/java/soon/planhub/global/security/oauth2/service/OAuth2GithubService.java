package soon.planhub.global.security.oauth2.service;

import java.util.Collections;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import soon.planhub.domain.member.entity.Member;
import soon.planhub.domain.member.repository.MemberRepository;
import soon.planhub.global.exception.dto.common.EntityNotFoundException;
import soon.planhub.global.security.oauth2.dto.CustomOAuth2Member;

@Slf4j
@RequiredArgsConstructor
@Service
public class OAuth2GithubService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String oauthAccessToken = userRequest.getAccessToken()
            .getTokenValue();
        String nameAttributeKey = userRequest.getClientRegistration()
            .getProviderDetails()
            .getUserInfoEndpoint()
            .getUserNameAttributeName();
        Member member = findOrCreateMember(oAuth2User, oauthAccessToken);

        return new CustomOAuth2Member(
            Collections.singleton(new SimpleGrantedAuthority(member.getRole().name())),
            oAuth2User.getAttributes(),
            nameAttributeKey,
            member.getId(),
            member.getNickname(),
            oauthAccessToken
        );
    }

    private Member findOrCreateMember(OAuth2User oAuth2User, String oauthAccessToken) {
        String nickname = oAuth2User.getName();
        try {
            return memberRepository.findByNickname(nickname);
        } catch (EntityNotFoundException e) {
            String profileImageURL = oAuth2User.getAttribute("avatar_url");
            String email = oAuth2User.getAttribute("email"); // TODO: 이메일 정보가 없을 경우 처리 필요
            Member member = Member.createOAuthMember(email, nickname, profileImageURL, oauthAccessToken);
            memberRepository.save(member);
            return member;
        }
    }

}