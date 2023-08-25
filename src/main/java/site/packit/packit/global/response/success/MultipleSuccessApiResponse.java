package site.packit.packit.global.response.success;

import lombok.Getter;

import java.util.List;

@Getter
public class MultipleSuccessApiResponse<T>
        extends SuccessApiResponse{

    private final List<T> data;

    private MultipleSuccessApiResponse(
            final String message,
            final List<T> data
    ) {
        super(message);
        this.data = data;
    }

    public static <T> MultipleSuccessApiResponse<T> of(
            final String message,
            final List<T> data
    ) {
        return new MultipleSuccessApiResponse<>(
                message,
                data
        );
    }
}