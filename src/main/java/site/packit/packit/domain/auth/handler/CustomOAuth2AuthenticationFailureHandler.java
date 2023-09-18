package site.packit.packit.domain.auth.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import site.packit.packit.domain.auth.repository.OAuth2AuthorizationRequestBasedOnCookieRepository;
import site.packit.packit.global.exception.ErrorCode;
import site.packit.packit.global.response.error.ErrorApiResponse;

import java.io.IOException;

import static site.packit.packit.domain.auth.exception.AuthErrorCode.INVALID_CREDENTIALS;

@Slf4j
@Component
public class CustomOAuth2AuthenticationFailureHandler
        implements AuthenticationFailureHandler {

    private final OAuth2AuthorizationRequestBasedOnCookieRepository authorizationRequestRepository;

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
        log.error("LoginFailureHandler.onAuthenticationFailure");

        authorizationRequestRepository.removeAuthorizationRequestCookies(request, response);

        log.error("LoginFailureHandler.onAuthenticationFailure");
        setResponse(
                response,
                INVALID_CREDENTIALS
        );
    }

    private void setResponse(
            HttpServletResponse response,
            ErrorCode errorCode
    ) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(
                ErrorApiResponse.of(errorCode).convertResponseToJson()
        );
    }
}