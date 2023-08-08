package site.packit.packit.domain.auth.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import site.packit.packit.global.exception.ErrorCode;

import static org.springframework.http.HttpStatus.*;

@Getter
public enum AuthErrorCode
        implements ErrorCode {

    /**
     * JWT
     * 1 ~ 999
     */
    INVALID_TOKEN("AT-C-0001", UNAUTHORIZED, "유효하지 않은 토큰입니다."),
    EXPIRED_TOKEN("AT-C-0002", UNAUTHORIZED, "만료된 토큰입니다."),
    NOT_EXPIRED_TOKEN("AT-C-0003", BAD_REQUEST, "만료되지 않은 토큰입니다."),
    REQUEST_TOKEN_NOT_FOUND("AT-C-0004", BAD_REQUEST, "요청에 토큰이 존재하지 않습니다."),
    INVALID_REFRESH_TOKEN("AT-C-0005", BAD_REQUEST, "유효하지 않은 리프레쉬 토큰입니다."),
    UNTRUSTED_CREDENTIAL("AT-C-0006", UNAUTHORIZED, "신뢰할 수 없는 자격증명 입니다."),
    LOGGED_OUT_TOKEN("AT-C-0007", UNAUTHORIZED, "로그아웃된 토큰입니다."),

    /**
     * Common Exception
     * 9000 ~ 9999
     */
    AUTHENTICATION_ERROR("AT-C-9000", UNAUTHORIZED, "Authentication exception."),
    INTERNAL_AUTHENTICATION_SERVICE_EXCEPTION("AT-S-9000", INTERNAL_SERVER_ERROR, "Internal authentication service exception.");

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;

    AuthErrorCode(
            String code,
            HttpStatus httpStatus,
            String message
    ) {
        this.code = code;
        this.httpStatus = httpStatus;
        this.message = message;
    }
}