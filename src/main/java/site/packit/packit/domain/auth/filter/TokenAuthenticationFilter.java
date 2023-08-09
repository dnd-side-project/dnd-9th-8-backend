package site.packit.packit.domain.auth.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import site.packit.packit.domain.auth.exception.TokenException;
import site.packit.packit.domain.auth.service.AuthService;
import site.packit.packit.domain.auth.token.AuthToken;
import site.packit.packit.domain.auth.token.AuthTokenProvider;
import site.packit.packit.global.util.HeaderUtil;

import java.io.IOException;

import static site.packit.packit.domain.auth.exception.AuthErrorCode.REQUEST_TOKEN_NOT_FOUND;

public class TokenAuthenticationFilter
        extends OncePerRequestFilter {

    private final AuthTokenProvider authTokenProvider;
    private final AuthService authService;

    private static final String EXCEPTION_ATTRIBUTE_NAME = "exceptionCode";

    private static final String TOKEN_REISSUE_REQUEST_URI = "/api/auth/refresh";

    public TokenAuthenticationFilter(
            AuthTokenProvider authTokenProvider,
            AuthService authService
    ) {
        this.authTokenProvider = authTokenProvider;
        this.authService = authService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        // Access Token 값 파싱
        String accessToken = HeaderUtil.getAccessToken(request);

        // 요청에 토큰이 존재하는지 검증
        if (accessToken == null) {
            request.setAttribute(EXCEPTION_ATTRIBUTE_NAME, REQUEST_TOKEN_NOT_FOUND);
            filterChain.doFilter(request, response);
            return;
        }

        AuthToken authAccessToken = authTokenProvider.createAuthTokenOfAccessToken(accessToken);

        // Token 재발급 요청인 경우
        String path = request.getRequestURI();
        if (TOKEN_REISSUE_REQUEST_URI.equals(path)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            authTokenProvider.tokenValidate(authAccessToken);
            authTokenProvider.checkLogoutToken(authAccessToken);

            SecurityContextHolder.getContext().setAuthentication(
                    authService.getAuthentication(authAccessToken)
            );
        } catch (TokenException e) {
            request.setAttribute(EXCEPTION_ATTRIBUTE_NAME, e.getErrorCode());
        }

        filterChain.doFilter(request, response);
    }
}