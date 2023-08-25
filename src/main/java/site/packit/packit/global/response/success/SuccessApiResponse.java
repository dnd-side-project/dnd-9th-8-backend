package site.packit.packit.global.response.success;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class SuccessApiResponse {

    private final String message;

    private final LocalDateTime timestamp = LocalDateTime.now();

    protected SuccessApiResponse(
            final String message
    ) {
        this.message = message;
    }

    public static SuccessApiResponse of(
            final String message
    ) {
        return new SuccessApiResponse(message);
    }
}