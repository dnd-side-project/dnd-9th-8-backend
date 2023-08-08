package site.packit.packit.domain.auth.exception;

import io.jsonwebtoken.Claims;
import site.packit.packit.global.exception.ErrorCode;

public class TokenException
        extends RuntimeException {

    private final ErrorCode errorCode;
    private Claims expiredTokenClaims;

    public TokenException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public TokenException(
            ErrorCode errorCode,
            Claims expiredTokenClaims
    ) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.expiredTokenClaims = expiredTokenClaims;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public Claims getExpiredTokenClaims() {
        return expiredTokenClaims;
    }
}