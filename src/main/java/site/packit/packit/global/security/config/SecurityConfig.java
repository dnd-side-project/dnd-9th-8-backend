package site.packit.packit.global.security.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import site.packit.packit.domain.auth.exception.CustomAuthenticationEntryPoint;
import site.packit.packit.domain.auth.repository.OAuth2AuthorizationRequestBasedOnCookieRepository;

import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

@Configuration
public class SecurityConfig {

    private final OAuth2AuthorizationRequestBasedOnCookieRepository oAuth2AuthorizationRequestBasedOnCookieRepository;
    private final OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService;
    private final AuthenticationSuccessHandler authenticationSuccessHandler;
    private final AuthenticationFailureHandler authenticationFailureHandler;
    private final LogoutSuccessHandler logoutSuccessHandler;

    private static final List<Map<String, String>> PERMIT_ALL_PATTERNS = List.of(
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
                    "METHOD", POST.name()
            ),
            Map.of(
                    "URL", "/api/check",
                    "METHOD", GET.name()
            )
    );

    private static final String SESSION_COOKIE_NAME = "PIID";

    public SecurityConfig(
            OAuth2AuthorizationRequestBasedOnCookieRepository oAuth2AuthorizationRequestBasedOnCookieRepository,
            OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService,
            AuthenticationSuccessHandler authenticationSuccessHandler,
            AuthenticationFailureHandler authenticationFailureHandler,
            LogoutSuccessHandler logoutSuccessHandler
    ) {
        this.oAuth2AuthorizationRequestBasedOnCookieRepository = oAuth2AuthorizationRequestBasedOnCookieRepository;
        this.oAuth2UserService = oAuth2UserService;
        this.authenticationSuccessHandler = authenticationSuccessHandler;
        this.authenticationFailureHandler = authenticationFailureHandler;
        this.logoutSuccessHandler = logoutSuccessHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http
                .formLogin(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable);

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
                        .successHandler(authenticationSuccessHandler)
                        .failureHandler(authenticationFailureHandler)
                );

        http
                .logout(logout -> logout
                        .logoutUrl("/api/logout")
                        .invalidateHttpSession(true)
                        .logoutSuccessHandler(logoutSuccessHandler)
                        .deleteCookies(SESSION_COOKIE_NAME)
                );

        http
                .authorizeHttpRequests(request -> request
                        .requestMatchers(
                                convertPermitPatternsToAntPathRequestMatchers()
                        )
                        .permitAll()
                        .anyRequest().authenticated()
                );

        http
                .exceptionHandling(handle -> handle
                        .authenticationEntryPoint(customAuthenticationEntryPoint())
                );

        return http.build();
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

    public AuthenticationEntryPoint customAuthenticationEntryPoint() {
        return new CustomAuthenticationEntryPoint();
    }
}