package site.packit.packit.domain.auth.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;
import site.packit.packit.global.response.success.SuccessApiResponse;

import java.io.IOException;

@Slf4j
@Component
public class CustomLogoutSuccessHandler
        implements LogoutSuccessHandler {

    @Override
    public void onLogoutSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {
        log.info("LoginSuccessHandler.onAuthenticationSuccess");

        setResponse(response);
    }

    private void setResponse(
            HttpServletResponse response
    ) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write(
                SuccessApiResponse.of("성공적으로 인증이 해제되었습니다.").convertResponseToJson()
        );
    }
}