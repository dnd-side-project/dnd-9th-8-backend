package site.packit.packit.domain.auth.service;

import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import site.packit.packit.domain.auth.exception.TokenException;
import site.packit.packit.domain.auth.info.CustomUserPrincipal;
import site.packit.packit.domain.auth.repository.RefreshTokenRepository;
import site.packit.packit.domain.auth.token.AuthToken;
import site.packit.packit.domain.auth.token.AuthTokenProvider;
import site.packit.packit.domain.member.entity.Member;
import site.packit.packit.domain.member.repository.MemberRepository;
import site.packit.packit.global.exception.ResourceNotFoundException;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import static site.packit.packit.domain.auth.exception.AuthErrorCode.LOGGED_OUT_TOKEN;
import static site.packit.packit.domain.member.exception.MemberErrorCode.MEMBER_NOT_FOUND;

@Service
public class AuthService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final MemberRepository memberRepository;
    private final AuthTokenProvider tokenProvider;

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

    // 토큰에서 파싱한 사용자 권한 리스트 문자열을 각각 분리해서 GrantedAuthority List 로 반환
    private Collection<? extends GrantedAuthority> getMemberAuthority(String memberRoles) {
        return Arrays.stream(memberRoles.split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    public void checkLogoutToken(Claims claims) {
        if (!refreshTokenRepository.existsByMemberPersonalId(claims.getSubject()))
            throw new TokenException(LOGGED_OUT_TOKEN);
    }
}