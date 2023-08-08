package site.packit.packit.global.response.success;

import lombok.Getter;

@Getter
public class SingleSuccessApiResponse<T>
        extends SuccessApiResponse {

    private final T data;

    private SingleSuccessApiResponse(
            final String message,
            final T data
    ) {
        super(message);
        this.data = data;
    }

    public static <T> SingleSuccessApiResponse<T> of(
            final String message,
            final T data
    ) {
        return new SingleSuccessApiResponse<>(
                message,
                data
        );
    }
}