package com.me.repository.impl;

import com.me.aws.AwsS3Connector;
import com.me.domain.Image;
import com.me.repository.ImageRepositoryS3;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.util.logging.Logger;

@Repository
public class ImageRepositoryS3Impl implements ImageRepositoryS3 {
    private static Logger log = Logger.getLogger(ImageRepositoryS3Impl.class.getName());

    @Value("${aws.bucket.name}")
    private String BUCKET_NAME;
    private final S3Client s3Client;

    @Autowired
    public ImageRepositoryS3Impl(AwsS3Connector awsS3Connector) {
        this.s3Client = awsS3Connector.getS3client();
    }

    @Override
    public Image downloadImageByName(String fullName) {
        log.info("ImageRepositoryCustom#downloadImageByName()");
        try {
            ResponseBytes<GetObjectResponse> objectAsBytes = s3Client.getObjectAsBytes(
                    GetObjectRequest.builder()
                            .bucket(BUCKET_NAME)
                            .key(fullName)
                            .build());

            Image image = new Image();
            image.setFullName(fullName);
            image.setContent(objectAsBytes.asInputStream());

            return image;
        } catch (Exception e) {
            throw new RuntimeException("Exception during ImageRepositoryS3Impl#downloadImageByName(String fullName)", e);
        }
    }

    @Override
    public Image uploadImage(Image image) {
        log.info("ImageRepositoryCustom#uploadImage");
        s3Client.putObject(
                PutObjectRequest.builder()
                        .bucket(BUCKET_NAME)
                        .key(image.getFullName())
                        .build(),
                RequestBody.fromInputStream(image.getContent(), image.getFileSize()));
        return image;
    }

    @Override
    public void deleteImageByName(String fullName) {
        log.info("ImageRepositoryCustom#deleteImageByName()");
        s3Client.deleteObject(
                DeleteObjectRequest.builder()
                        .bucket(BUCKET_NAME)
                        .key(fullName)
                        .build());
    }
}
