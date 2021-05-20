package com.me.aws;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import javax.annotation.PostConstruct;
import java.util.logging.Logger;

import static com.me.aws.CredentialsStateHolder.AWS_ACCESS_KEY_ID;
import static com.me.aws.CredentialsStateHolder.AWS_SECRET_KEY;

@Component
public class AwsS3Connector {
    private static Logger log = Logger.getLogger(AwsS3Connector.class.getName());
    @Value("${aws.region}")
    private String REGION_PROP;

    private S3Client s3client;

    @PostConstruct
    private void setUpS3Client() {
        final Region region = Region.of(REGION_PROP);

        final AwsBasicCredentials credentials = AwsBasicCredentials.create(
                AWS_ACCESS_KEY_ID.getValue(),
                AWS_SECRET_KEY.getValue());

        s3client = S3Client.builder()
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .region(region)
                .build();
        log.info("S3Client is built. Region=" + region);
    }

    public S3Client getS3client() {
        return s3client;
    }
}
