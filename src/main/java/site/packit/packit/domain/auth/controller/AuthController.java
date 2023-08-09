package site.packit.packit.domain.auth.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.packit.packit.domain.auth.dto.ReissueTokenResponse;
import site.packit.packit.domain.auth.exception.TokenException;
import site.packit.packit.domain.auth.service.AuthService;
import site.packit.packit.global.response.success.SingleSuccessApiResponse;
import site.packit.packit.global.response.success.SuccessApiResponse;
import site.packit.packit.global.util.CookieUtil;
import site.packit.packit.global.util.HeaderUtil;

import java.util.Map;

import static site.packit.packit.domain.auth.exception.AuthErrorCode.REQUEST_TOKEN_NOT_FOUND;

@RequestMapping("/api/auth")
@RestController
public class AuthController {

    private final AuthService authService;

    @Value("${app.jwt.expiry.refresh-token-expiry}")
    private long refreshTokenExpiry;

    @Value("${app.cookie.refresh-token-cookie-name}")
    private String refreshTokenCookieName;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Access Token 재발급
     */
    @PostMapping("/refresh")
    public ResponseEntity<SingleSuccessApiResponse<ReissueTokenResponse>> reissueAccessToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        String accessToken = HeaderUtil.getAccessToken(request);

        if (accessToken == null) {
            throw new TokenException(REQUEST_TOKEN_NOT_FOUND);
        }

        String refreshToken = CookieUtil.getCookie(request, refreshTokenCookieName)
                .map(Cookie::getValue)
                .orElse(null);

        Map<String, String> newTokens = authService.reissueToken(accessToken, refreshToken);

        int cookieMaxAge = (int) refreshTokenExpiry / 60;
        CookieUtil.deleteCookie(request, response, refreshTokenCookieName);
        CookieUtil.addCookie(
                response,
                refreshTokenCookieName,
                newTokens.get("refreshToken"),
                cookieMaxAge
        );

        return ResponseEntity.ok(
                SingleSuccessApiResponse.of(
                        "성공적으로 토큰이 재발급되었습니다.",
                        ReissueTokenResponse.of(newTokens.get("accessToken"))
                )
        );
    }

    /**
     * Logout
     */
    @DeleteMapping("/logout")
    public ResponseEntity<SuccessApiResponse> logout(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        authService.logout(request, response);

        return ResponseEntity.ok(
                SuccessApiResponse.of(
                        "성공적으로 로그아웃되었습니다."
                )
        );
    }
}