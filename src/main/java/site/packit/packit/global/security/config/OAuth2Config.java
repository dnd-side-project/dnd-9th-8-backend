package site.packit.packit.global.security.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import site.packit.packit.domain.auth.handler.CustomOAuth2AuthenticationFailureHandler;
import site.packit.packit.domain.auth.handler.CustomOAuth2AuthenticationSuccessHandler;
import site.packit.packit.domain.auth.repository.OAuth2AuthorizationRequestBasedOnCookieRepository;
import site.packit.packit.domain.auth.repository.RefreshTokenRepository;
import site.packit.packit.domain.auth.token.AuthTokenProvider;

import java.util.List;

@Configuration
public class OAuth2Config {

    private final AuthTokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${app.oauth2.authorized-redirect-uris}")
    private List<String> authorizedRedirectUris;

    @Value("${app.oauth2.cookie-max-age}")
    private Integer cookieMaxAge;

    public OAuth2Config(
            AuthTokenProvider tokenProvider,
            RefreshTokenRepository refreshTokenRepository
    ) {
        this.tokenProvider = tokenProvider;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    /**
     * 쿠키 기반 인가 Repository
     */
    @Bean
    public OAuth2AuthorizationRequestBasedOnCookieRepository oAuth2AuthorizationRequestBasedOnCookieRepository() {
        return new OAuth2AuthorizationRequestBasedOnCookieRepository();
    }

    /**
     * OAuth2 인증 성공 핸들러
     */
    @Bean
    public CustomOAuth2AuthenticationSuccessHandler customOAuth2AuthenticationSuccessHandler() {
        return new CustomOAuth2AuthenticationSuccessHandler(
                tokenProvider,
                refreshTokenRepository,
                oAuth2AuthorizationRequestBasedOnCookieRepository(),
                authorizedRedirectUris,
                cookieMaxAge
        );
    }

    /**
     * OAuth2 인증 실패 핸들러
     */
    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new CustomOAuth2AuthenticationFailureHandler(
                oAuth2AuthorizationRequestBasedOnCookieRepository()
        );
    }
}