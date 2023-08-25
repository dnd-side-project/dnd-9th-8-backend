package site.packit.packit.domain.auth.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import site.packit.packit.global.exception.ErrorCode;
import site.packit.packit.global.response.error.ErrorApiResponse;

@Slf4j
@RestControllerAdvice
public class AuthExceptionHandler {

    @ExceptionHandler
    protected ResponseEntity<ErrorApiResponse> handleTokenException(
            TokenException e
    ) {
        ErrorCode errorCode = e.getErrorCode();
        log.error("[handle TokenException] - {}", e.getMessage());
        return new ResponseEntity<>(
                ErrorApiResponse.of(errorCode, errorCode.getMessage()),
                errorCode.getHttpStatus()
        );
    }
}