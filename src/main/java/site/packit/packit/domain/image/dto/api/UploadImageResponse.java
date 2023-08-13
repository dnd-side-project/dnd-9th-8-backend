package site.packit.packit.domain.image.dto.api;

public record UploadImageResponse(
        String savedImageUrl
) {
    public static UploadImageResponse of(
            String savedImageUrl
    ) {
        return new UploadImageResponse(savedImageUrl);
    }
}