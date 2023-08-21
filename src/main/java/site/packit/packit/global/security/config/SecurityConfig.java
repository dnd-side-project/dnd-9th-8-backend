package site.packit.packit.global.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import site.packit.packit.domain.auth.exception.CustomAuthenticationEntryPoint;
import site.packit.packit.domain.auth.filter.TokenAuthenticationFilter;
import site.packit.packit.domain.auth.handler.CustomOAuth2AuthenticationSuccessHandler;
import site.packit.packit.domain.auth.handler.TokenAccessDeniedHandler;
import site.packit.packit.domain.auth.repository.OAuth2AuthorizationRequestBasedOnCookieRepository;
import site.packit.packit.domain.auth.service.AuthService;
import site.packit.packit.domain.auth.token.AuthTokenProvider;

import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

@Configuration
public class SecurityConfig {

    private final AuthTokenProvider authTokenProvider;
    private final AuthService authService;
    private final OAuth2AuthorizationRequestBasedOnCookieRepository oAuth2AuthorizationRequestBasedOnCookieRepository;
    private final OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService;
    private final CustomOAuth2AuthenticationSuccessHandler customOAuth2AuthenticationSuccessHandler;
    private final AuthenticationFailureHandler authenticationFailureHandler;

    private static final List<Map<String, String>> PERMIT_ALL_PATTERNS = List.of(
            Map.of(
                    "URL", "/api/auth/refresh",
                    "METHOD", POST.name()
            ),
            Map.of(
                    "URL", "/docs/**",
                    "METHOD", GET.name()
            ),
            Map.of(
                    "URL", "/api/dev-free",
                    "METHOD", GET.name()
            ),
            Map.of(
                    "URL", "/api/email-authentication",
                    "METHOD", GET.name()
            ),
            Map.of(
                    "URL", "/api/check",
                    "METHOD", GET.name()
            )
    );

    public SecurityConfig(
            AuthTokenProvider authTokenProvider,
            AuthService authService,
            OAuth2AuthorizationRequestBasedOnCookieRepository oAuth2AuthorizationRequestBasedOnCookieRepository,
            OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService,
            CustomOAuth2AuthenticationSuccessHandler customOAuth2AuthenticationSuccessHandler,
            AuthenticationFailureHandler authenticationFailureHandler
    ) {
        this.authTokenProvider = authTokenProvider;
        this.authService = authService;
        this.oAuth2AuthorizationRequestBasedOnCookieRepository = oAuth2AuthorizationRequestBasedOnCookieRepository;
        this.oAuth2UserService = oAuth2UserService;
        this.customOAuth2AuthenticationSuccessHandler = customOAuth2AuthenticationSuccessHandler;
        this.authenticationFailureHandler = authenticationFailureHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http
                .formLogin(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable);

        http
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        http
                .addFilterBefore(
                        tokenAuthenticationFilter(),
                        UsernamePasswordAuthenticationFilter.class
                )
                .exceptionHandling(handle -> handle
                        .authenticationEntryPoint(customAuthenticationEntryPoint())
                        .accessDeniedHandler(tokenAccessDeniedHandler())
                );

        http
                .oauth2Login(login -> login
                        .authorizationEndpoint(endPoint -> endPoint
                                .baseUri("/api/oauth2/authorization")
                                .authorizationRequestRepository(oAuth2AuthorizationRequestBasedOnCookieRepository)
                        )
                        .redirectionEndpoint(endPoint -> endPoint
                                .baseUri("/*/oauth2/code/*")
                        )
                        .userInfoEndpoint(endPoint -> endPoint
                                .userService(oAuth2UserService)
                        )
                        .successHandler(customOAuth2AuthenticationSuccessHandler)
                        .failureHandler(authenticationFailureHandler)
                );

        http
                .authorizeHttpRequests(request -> request
                        .requestMatchers(
                                convertPermitPatternsToAntPathRequestMatchers()
                        )
                        .permitAll()
                        .anyRequest().authenticated()
                );

        return http.build();
    }

    /**
     * Authentication Entry Point
     */
    public CustomAuthenticationEntryPoint customAuthenticationEntryPoint() {
        return new CustomAuthenticationEntryPoint();
    }

    /**
     * 토큰 검증 오류 처리 핸들러
     */
    public TokenAccessDeniedHandler tokenAccessDeniedHandler() {
        return new TokenAccessDeniedHandler();
    }

    private TokenAuthenticationFilter tokenAuthenticationFilter() {
        return new TokenAuthenticationFilter(
                authTokenProvider,
                authService
        );
    }

    private AntPathRequestMatcher[] convertPermitPatternsToAntPathRequestMatchers() {
        return PERMIT_ALL_PATTERNS.stream()
                .map(pattern -> {
                    return new AntPathRequestMatcher(
                            pattern.get("URL"),
                            pattern.get("METHOD")
                    );
                })
                .toArray(AntPathRequestMatcher[]::new);
    }
}