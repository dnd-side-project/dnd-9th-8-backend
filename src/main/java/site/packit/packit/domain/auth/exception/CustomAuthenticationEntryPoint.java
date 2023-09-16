package site.packit.packit.domain.auth.exception;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import site.packit.packit.global.response.error.ErrorApiResponse;

import java.io.IOException;

import static site.packit.packit.domain.auth.exception.AuthErrorCode.REQUEST_AUTHENTICATION;

public class CustomAuthenticationEntryPoint
        implements AuthenticationEntryPoint {

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws IOException, ServletException {
        setResponse(response);
    }

    private void setResponse(
            HttpServletResponse response
    ) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().print(ErrorApiResponse.of(REQUEST_AUTHENTICATION).convertResponseToJson());
    }
}