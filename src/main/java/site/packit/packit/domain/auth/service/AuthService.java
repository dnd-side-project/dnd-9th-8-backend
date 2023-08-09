package site.packit.packit.domain.auth.service;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.packit.packit.domain.auth.exception.TokenException;
import site.packit.packit.domain.auth.info.CustomUserPrincipal;
import site.packit.packit.domain.auth.repository.RefreshTokenRepository;
import site.packit.packit.domain.auth.token.AuthToken;
import site.packit.packit.domain.auth.token.AuthTokenProvider;
import site.packit.packit.domain.auth.token.RefreshToken;
import site.packit.packit.domain.member.entity.Member;
import site.packit.packit.domain.member.repository.MemberRepository;
import site.packit.packit.global.exception.ResourceNotFoundException;
import site.packit.packit.global.util.CookieUtil;
import site.packit.packit.global.util.HeaderUtil;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import static site.packit.packit.domain.auth.exception.AuthErrorCode.*;
import static site.packit.packit.domain.member.exception.MemberErrorCode.MEMBER_NOT_FOUND;

@Service
public class AuthService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final MemberRepository memberRepository;
    private final AuthTokenProvider tokenProvider;

    @Value("${app.cookie.refresh-token-cookie-name}")
    private String refreshTokenCookieName;

    public AuthService(
            RefreshTokenRepository refreshTokenRepository,
            MemberRepository memberRepository,
            AuthTokenProvider tokenProvider
    ) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.memberRepository = memberRepository;
        this.tokenProvider = tokenProvider;
    }

    /**
     * 인가된 사용자 인증 정보 조회
     */
    public Authentication getAuthentication(AuthToken accessToken) {
        // access token 검증
        tokenProvider.tokenValidate(accessToken);

        // access token claims 조회
        Claims claims = accessToken.getTokenClaims();

        // claims 값을 기반으로 Member Entity 조회
        Member member = memberRepository.findByPersonalId(claims.getSubject())
                .orElseThrow(() -> new ResourceNotFoundException(MEMBER_NOT_FOUND));

        // claims 값을 기반으로 사용자의 권한 조회
        Collection<? extends GrantedAuthority> roles = getMemberAuthority(
                claims.get("roles", String.class)
        );
        UserDetails userPrincipal = CustomUserPrincipal.from(member, roles);

        return new UsernamePasswordAuthenticationToken(
                userPrincipal,
                accessToken,
                userPrincipal.getAuthorities()
        );
    }

    private Collection<? extends GrantedAuthority> getMemberAuthority(String memberRoles) {
        return Arrays.stream(memberRoles.split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    public void checkLogoutToken(Claims claims) {
        if (!refreshTokenRepository.existsByMemberPersonalId(claims.getSubject()))
            throw new TokenException(LOGGED_OUT_TOKEN);
    }

    /**
     * Refresh Token 확인 후 토큰 전체 재발급
     */
    @Transactional
    public Map<String, String> reissueToken(
            String expiredAccessToken,
            String refreshToken
    ) {

        AuthToken authTokenOfAccessToken = tokenProvider.createAuthTokenOfAccessToken(expiredAccessToken);
        Claims expiredTokenClaims = authTokenOfAccessToken.getExpiredTokenClaims();
        String memberPersonalId = expiredTokenClaims.getSubject();
        Collection<? extends GrantedAuthority> memberRoles = getMemberAuthority(expiredTokenClaims.get("roles", String.class));

        AuthToken authTokenOfRefreshToken = tokenProvider.createAuthTokenOfRefreshToken(refreshToken);

        // Refresh Token 검증
        try {
            tokenProvider.tokenValidate(authTokenOfRefreshToken);
        } catch (TokenException e) {
            throw new TokenException(INVALID_REFRESH_TOKEN);
        }

        RefreshToken storedRefreshToken = refreshTokenRepository.findByMemberPersonalIdAndValue(memberPersonalId, refreshToken)
                .orElseThrow(() -> new ResourceNotFoundException(REQUEST_TOKEN_NOT_FOUND));

        AuthToken newAccessToken = tokenProvider.generateAccessToken(memberPersonalId, memberRoles);

        AuthToken newRefreshToken = tokenProvider.generateRefreshToken(memberPersonalId, memberRoles);
        storedRefreshToken.updateValue(newRefreshToken.getValue());

        return Map.of(
                "accessToken", newAccessToken.getValue(),
                "refreshToken", newRefreshToken.getValue()
        );
    }

    @Transactional
    public void logout(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        refreshTokenRepository.deleteAllByMemberPersonalId(
                tokenProvider.createAuthTokenOfAccessToken(
                        HeaderUtil.getAccessToken(request)
                ).getTokenClaims().getSubject()
        );

        CookieUtil.deleteCookie(request, response, refreshTokenCookieName);
    }
}