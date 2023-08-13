package site.packit.packit.domain.image.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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
        String originalFilename = image.getOriginalFilename();

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(image.getSize());
        metadata.setContentType(image.getContentType());

        amazonS3.putObject(
                bucket,
                originalFilename,
                image.getInputStream(),
                metadata
        );
        return amazonS3.getUrl(bucket, originalFilename).toString();
    }

    @Override
    public void deleteImage(String originalFilename) {
        amazonS3.deleteObject(
                bucket,
                originalFilename
        );
    }
}