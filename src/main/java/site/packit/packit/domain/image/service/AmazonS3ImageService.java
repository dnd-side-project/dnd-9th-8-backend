package site.packit.packit.domain.image.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

@Service
public class AmazonS3ImageService
        implements ImageService {

    private final AmazonS3 amazonS3;

    public AmazonS3ImageService(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Override
    public String uploadImage(MultipartFile image) throws IOException {
        String storedImageName = getStoredImageName(Objects.requireNonNull(image.getOriginalFilename()));

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(image.getSize());
        metadata.setContentType(image.getContentType());

        amazonS3.putObject(
                bucket,
                storedImageName,
                image.getInputStream(),
                metadata
        );
        return amazonS3.getUrl(bucket, storedImageName).toString();
    }

    @Override
    public void deleteImage(String storedImageFullPath) throws Exception {

        String storedImageResourcePath = getStoredImageResourcePath(storedImageFullPath);

        // 삭제할 파일이 존재하는지 검증
        if (!amazonS3.doesObjectExist(
                bucket,
                storedImageResourcePath
        )) {
            throw new Exception("이미지가 존재하지 않습니다.");
        }

        amazonS3.deleteObject(
                bucket,
                storedImageFullPath
        );
    }

    private String getStoredImageName(
            String originalFileName
    ) {
        String uuid = UUID.randomUUID().toString();
        String imageExtension = originalFileName
                .substring(originalFileName.lastIndexOf(".") + 1);
        return uuid + "." + imageExtension;
    }

    private String getStoredImageResourcePath(String storedImageFullPath) {
        return storedImageFullPath.substring(storedImageFullPath.lastIndexOf(".com/") + 5);
    }
}