package site.packit.packit.domain.member.exception;

import site.packit.packit.global.exception.ErrorCode;

import static site.packit.packit.domain.auth.exception.AuthErrorCode.INVALID_REDIRECT_URI;

public class InvalidRedirectUriException
        extends RuntimeException {

    private final ErrorCode exceptionCode;

    public InvalidRedirectUriException() {
        super(INVALID_REDIRECT_URI.getMessage());
        this.exceptionCode = INVALID_REDIRECT_URI;
    }

    public ErrorCode getExceptionCode() {
        return exceptionCode;
    }
}