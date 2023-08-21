package site.packit.packit.domain.auth.email.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import site.packit.packit.global.exception.ErrorCode;

import static org.springframework.http.HttpStatus.*;

@Getter
public enum EmailErrorCode
        implements ErrorCode {

    INVALID_AUTHENTICATION_CODE("EA-C-0001", UNAUTHORIZED, "유효하지 않은 인증 코드입니다."),
    EXPIRED_AUTHENTICATION_CODE("EA-C-0002", UNAUTHORIZED, "만료 인증 코드입니다."),
    ALREADY_EMAIL_AUTHORIZED("EA-C-0003", BAD_REQUEST, "이미 이메일 인증이 완료된 사용자입니다.");

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;

    EmailErrorCode(
            String code,
            HttpStatus httpStatus,
            String message
    ) {
        this.code = code;
        this.httpStatus = httpStatus;
        this.message = message;
    }
}