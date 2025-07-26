package soon.planhub.global.security.jwt.provider;

import static soon.planhub.domain.member.entity.Role.ROLE_USER;
import static soon.planhub.global.security.jwt.common.TokenExpiration.ACCESS_TOKEN;
import static soon.planhub.global.security.jwt.common.TokenExpiration.REFRESH_TOKEN;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import java.security.Key;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import soon.planhub.domain.member.entity.Role;
import soon.planhub.global.security.jwt.dto.response.TokenResponse;

@Slf4j
@Component
public class JwtProvider {

    private static final String ROLE_CLAIM_KEY = "auth";
    private final Key key;

    public JwtProvider(@Value("${spring.jwt.secretKey}") String secretKey) {
        byte[] decode = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(decode);
    }

    public boolean validateToken(String token) {
        try {
            getClaimsFromToken(token);
            return true;
        } catch (SignatureException | MalformedJwtException e) {
            log.warn("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
        } catch (ExpiredJwtException e) {
            log.warn("Expired JWT, 만료된 JWT 입니다.");
        } catch (UnsupportedJwtException e) {
            log.warn("Unsupported JWT, 지원되지 않는 JWT 입니다.");
        } catch (IllegalArgumentException e) {
            log.warn("JWT claims is empty, 잘못된 JWT 입니다.");
        }
        return false;
    }

    public TokenResponse generateAllToken(Long memberId) {
        String accessToken = generateAccessToken(memberId);
        String refreshToken = generateRefreshToken();

        return TokenResponse.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .build();
    }

    public String getSubjectFromToken(String token) {
        return getClaimsFromToken(token).getSubject();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = getClaimsFromToken(token);
        String memberId = claims.getSubject();

        Role role = getRoleFromToken(claims);
        Collection<SimpleGrantedAuthority> authorities = Collections.singletonList(
            new SimpleGrantedAuthority(role.name())
        );

        return new UsernamePasswordAuthenticationToken(memberId, "", authorities);
    }

    private Role getRoleFromToken(Claims claims) {
        return Role.valueOf(claims.get(ROLE_CLAIM_KEY, String.class));
    }

    private String generateAccessToken(Long memberId) {
        Date expirationDate = createExpirationDate(ACCESS_TOKEN.getExpirationTime());
        return Jwts.builder()
            .setSubject(String.valueOf(memberId))
            .claim(ROLE_CLAIM_KEY, ROLE_USER.name())
            .setExpiration(expirationDate)
            .setIssuedAt(new Date())
            .signWith(key, SignatureAlgorithm.HS512)
            .compact();
    }

    private String generateRefreshToken() {
        Date expirationDate = createExpirationDate(REFRESH_TOKEN.getExpirationTime());
        return Jwts.builder()
            .setExpiration(expirationDate)
            .signWith(key, SignatureAlgorithm.HS512)
            .compact();
    }

    private Date createExpirationDate(long expirationTime) {
        long currentTimeMillis = System.currentTimeMillis();
        return new Date(currentTimeMillis + expirationTime);
    }

    private Claims getClaimsFromToken(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .getBody();
    }

}