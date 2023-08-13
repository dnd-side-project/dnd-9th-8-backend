package site.packit.packit.domain.image.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import site.packit.packit.domain.image.dto.api.UploadImageResponse;
import site.packit.packit.domain.image.service.ImageService;
import site.packit.packit.global.response.success.SingleSuccessApiResponse;

import java.io.IOException;

@RestController
public class ImageController {

    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping("/api/images")
    public ResponseEntity<SingleSuccessApiResponse<UploadImageResponse>> uploadImage(
            MultipartFile image
    ) throws IOException {
        String savedImageUrl = imageService.uploadImage(image);
        return ResponseEntity.ok(
                SingleSuccessApiResponse.of(
                        "성공적으로 이미지가 업로드되었습니다.",
                        UploadImageResponse.of(savedImageUrl)
                )
        );
    }
}