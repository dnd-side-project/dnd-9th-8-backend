package site.packit.packit.domain.auth.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.web.util.UriComponentsBuilder;
import site.packit.packit.domain.auth.repository.OAuth2AuthorizationRequestBasedOnCookieRepository;
import site.packit.packit.global.util.CookieUtil;

import java.io.IOException;

public class CustomOAuth2AuthenticationFailureHandler
        extends SimpleUrlAuthenticationFailureHandler {

    private final OAuth2AuthorizationRequestBasedOnCookieRepository authorizationRequestRepository;

    @Value("${app.cookie.redirect-uri-param-cookie-name}")
    private String REDIRECT_URI_PARAM_COOKIE_NAME;

    public CustomOAuth2AuthenticationFailureHandler(
            OAuth2AuthorizationRequestBasedOnCookieRepository authorizationRequestRepository
    ) {
        this.authorizationRequestRepository = authorizationRequestRepository;
    }

    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception
    ) throws IOException, ServletException {
        String targetUrl = CookieUtil.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue)
                .orElse(("/"));

        exception.printStackTrace();

        targetUrl = UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("error", exception.getLocalizedMessage())
                .build()
                .toUriString();

        authorizationRequestRepository.removeAuthorizationRequestCookies(request, response);

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}