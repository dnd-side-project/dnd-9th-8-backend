package site.packit.packit.domain.auth.exception;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import site.packit.packit.global.exception.ErrorCode;
import site.packit.packit.global.response.error.ErrorApiResponse;

import java.io.IOException;

import static site.packit.packit.domain.auth.exception.AuthErrorCode.AUTHENTICATION_ERROR;
import static site.packit.packit.domain.auth.exception.AuthErrorCode.UNTRUSTED_CREDENTIAL;

@Slf4j
public class CustomAuthenticationEntryPoint
        implements AuthenticationEntryPoint {

    private static final String EXCEPTION_ATTRIBUTE_NAME = "exceptionCode";

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws IOException, ServletException {

        ErrorCode exceptionCode = (ErrorCode) request.getAttribute(EXCEPTION_ATTRIBUTE_NAME);

        if (exceptionCode != null) {
            setResponse(response, exceptionCode);
        } else if (authException.getClass() == InsufficientAuthenticationException.class){
            authException.printStackTrace();
           setResponse(response, UNTRUSTED_CREDENTIAL);
        } else {
            // Error Message 만 넘어온 경우 범용 인증 예외 코드를 설정
            log.error("Responding with unauthorized error. Message := {}", authException.getMessage());
            setResponse(response, AUTHENTICATION_ERROR);
        }
    }

    // 에러 응답 생성 메소드
    private void setResponse(
            HttpServletResponse response,
            ErrorCode exceptionCode
    ) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        // 한글 출력을 위해 getWriter() 사용
        response.getWriter().print(ErrorApiResponse.of(exceptionCode).convertObjectToJson());
    }
}