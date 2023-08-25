package site.packit.packit.domain.auth.exception;

import site.packit.packit.global.exception.ErrorCode;

import static site.packit.packit.domain.auth.exception.AuthErrorCode.LOGIN_PROVIDER_MISMATCH;

public class OAuth2ProviderMisMatchException
        extends RuntimeException {

    private final ErrorCode errorCode;

    public OAuth2ProviderMisMatchException(String authenticationProvider) {
        super(LOGIN_PROVIDER_MISMATCH.getMessage() + "이미 " + authenticationProvider + "계정으로 가입되어 있습니다.");
        errorCode = LOGIN_PROVIDER_MISMATCH;
    }

    public ErrorCode getExceptionCode() {
        return errorCode;
    }
}