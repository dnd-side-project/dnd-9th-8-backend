package site.packit.packit.domain.auth.email.exception;

import lombok.Getter;
import site.packit.packit.global.exception.ErrorCode;

@Getter
public class EmailAuthenticationException
        extends RuntimeException {

    private final ErrorCode errorCode;

    public EmailAuthenticationException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public EmailAuthenticationException(
            Throwable cause,
            ErrorCode errorCode
    ) {
        super(cause);
        this.errorCode = errorCode;
    }
}