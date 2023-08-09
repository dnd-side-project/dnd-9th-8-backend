package site.packit.packit.domain.auth.exception;

import site.packit.packit.global.exception.ErrorCode;

import static site.packit.packit.domain.auth.exception.AuthErrorCode.INVALID_AUTHENTICATION_PROVIDER;

public class InvalidAuthenticationProviderException
        extends RuntimeException {

    private final ErrorCode errorCode;

    public InvalidAuthenticationProviderException() {
        super(INVALID_AUTHENTICATION_PROVIDER.getMessage());
        errorCode = INVALID_AUTHENTICATION_PROVIDER;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}