package site.packit.packit.domain.auth.email.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import site.packit.packit.global.response.error.ErrorApiResponse;

@Slf4j
@RestControllerAdvice
public class EmailExceptionHandler {

    @ExceptionHandler
    protected ResponseEntity<ErrorApiResponse> handleInvalidEmailAuthenticationException(
            EmailAuthenticationException e
    ) {
        log.error("[handle InvalidEmailAuthenticationException] - {}", e.getMessage());
        return new ResponseEntity<>(
                ErrorApiResponse.of(e.getErrorCode()),
                e.getErrorCode().getHttpStatus()
        );
    }
}