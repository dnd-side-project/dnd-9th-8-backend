package site.packit.packit.domain.item.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import site.packit.packit.global.exception.ErrorCode;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Getter
public enum ItemErrorCode implements ErrorCode {

    ITEM_NOT_FOUND("IT-C-001", NOT_FOUND, "존재하지 않는 체크리스트입니다.");

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;

    ItemErrorCode(
            String code,
            HttpStatus httpStatus,
            String message
    ) {
        this.code = code;
        this.httpStatus = httpStatus;
        this.message = message;
    }
}

