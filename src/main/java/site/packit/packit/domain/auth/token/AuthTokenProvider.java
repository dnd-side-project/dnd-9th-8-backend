package site.packit.packit.domain.auth.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority;
import site.packit.packit.domain.auth.exception.TokenException;
import site.packit.packit.domain.auth.repository.RefreshTokenRepository;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Collection;
import java.util.Date;

import static site.packit.packit.domain.auth.exception.AuthErrorCode.INVALID_TOKEN;
import static site.packit.packit.domain.auth.exception.AuthErrorCode.LOGGED_OUT_TOKEN;

public class AuthTokenProvider {
    private final Key accessTokenSecretKey;
    private final long accessTokenExpiry;
    private final Key refreshTokenSecretKey;
    private final long refreshTokenExpiry;
    private final RefreshTokenRepository refreshTokenRepository;


    public AuthTokenProvider(
            String accessTokenSecretKeyString,
            long accessTokenExpiry,
            String refreshTokenSecretKeyString,
            long refreshTokenExpiry,
            RefreshTokenRepository refreshTokenRepository
    ) {
        this.accessTokenSecretKey = generateTokenKey(accessTokenSecretKeyString);
        this.accessTokenExpiry = accessTokenExpiry;
        this.refreshTokenSecretKey = generateTokenKey(refreshTokenSecretKeyString);
        this.refreshTokenExpiry = refreshTokenExpiry;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    // token key 문자열을 Key Object 로 변환
    private Key generateTokenKey(String tokenKey) {
        return Keys.hmacShaKeyFor(tokenKey.getBytes(StandardCharsets.UTF_8));
    }

    // token 만료 정수를 Date Object 로 변환
    private Date generateTokenExpiry(
            long tokenExpiry
    ) {
        return new Date(new Date().getTime() + tokenExpiry);
    }

    /**
     * Access Token 생성
     */
    public AuthToken generateAccessToken(
            String memberPersonalId,
            Collection<? extends GrantedAuthority> memberRoles
    ) {
        return AuthToken.of(
                memberPersonalId,
                memberRoles,
                generateTokenExpiry(accessTokenExpiry),
                accessTokenSecretKey
        );
    }

    /**
     * Refresh Token 생성
     */
    public AuthToken generateRefreshToken(
            String memberPersonalId,
            Collection<? extends GrantedAuthority> memberRoles
    ) {
        return AuthToken.of(
                memberPersonalId,
                memberRoles,
                generateTokenExpiry(refreshTokenExpiry),
                refreshTokenSecretKey
        );
    }

    /**
     * Access Token 값을 기반으로 AuthToken Object 생성
     */
    public AuthToken createAuthTokenOfAccessToken(String accessToken) {
        return AuthToken.of(accessToken, accessTokenSecretKey);
    }

    /**
     * Refresh Token 값을 기반으로 AuthToken 객체 생성
     */
    public AuthToken createAuthTokenOfRefreshToken(String refreshToken) {
        return AuthToken.of(refreshToken, refreshTokenSecretKey);
    }

    /**
     * Token 유효성 검증
     */
    public void tokenValidate(AuthToken token) {
        Claims tokenClaims = token.getTokenClaims();
        if (tokenClaims == null)
            throw new TokenException(INVALID_TOKEN);
    }

    /**
     * 로그아웃된 토큰인지 체크
     */
    public void checkLogoutToken(AuthToken authToken) {
        if (!refreshTokenRepository.existsByMemberPersonalId(authToken.getTokenClaims().getSubject()))
            throw new TokenException(LOGGED_OUT_TOKEN);
    }
}