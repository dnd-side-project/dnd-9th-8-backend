package site.packit.packit.domain.auth.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import site.packit.packit.global.exception.ErrorCode;

import static org.springframework.http.HttpStatus.*;

@Getter
public enum AuthErrorCode
        implements ErrorCode {

    /**
     * AUTH
     * 1 ~ 999
     */
    INVALID_CREDENTIALS("AT-C-0001", UNAUTHORIZED, "유효하지 않은 인증 정보입니다."),
    REQUEST_AUTHENTICATION("AT-C-0002", UNAUTHORIZED, "인증이 필요한 요청입니다."),

    /**
     * OAuth2
     * 1000 ~ 1999
     */
    LOGIN_PROVIDER_MISMATCH("AT-C-1000", BAD_REQUEST, "잘못된 OAuth2 인증입니다."),
    INVALID_AUTHENTICATION_PROVIDER("AT-C-1001", BAD_REQUEST, "유효하지 않은 로그인 제공자입니다."),
    INVALID_MEMBER_ROLE("AT-C-1002", FORBIDDEN, "유효하지 않은 사용자 권한입니다."),

    /**
     * Common Exception
     * 9000 ~ 9999
     */
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