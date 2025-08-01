package soon.planhub.domain.auth.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import soon.planhub.domain.auth.service.dto.request.ReissueTokenServiceRequest;
import soon.planhub.domain.member.entity.Member;
import soon.planhub.domain.member.repository.MemberRepository;
import soon.planhub.global.exception.dto.ErrorDetail;
import soon.planhub.global.exception.dto.member.InvalidRefreshTokenException;
import soon.planhub.global.security.jwt.dto.response.TokenResponse;
import soon.planhub.global.security.jwt.provider.JwtProvider;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;

    @Transactional
    public TokenResponse reissueToken(ReissueTokenServiceRequest request) {
        String accessToken = request.accessToken();
        String refreshToken = request.refreshToken();
        Long memberId = Long.valueOf(jwtProvider.getSubjectFromToken(accessToken));

        validateRefreshToken(refreshToken, memberId);

        TokenResponse tokenResponse = jwtProvider.generateAllToken(memberId);
        Member member = memberRepository.findById(memberId);
        member.updateRefreshToken(tokenResponse.refreshToken());

        return tokenResponse;
    }

    private void validateRefreshToken(String refreshToken, Long memberId) {
        if (!memberRepository.existsByIdAndRefreshToken(memberId, refreshToken)) {
            throw new InvalidRefreshTokenException(ErrorDetail.INVALID_TOKEN);
        }
    }

}