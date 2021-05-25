package com.me.aws.provider;

import com.me.aws.AbstractAwsProvider;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import javax.annotation.PostConstruct;
import java.util.logging.Logger;

@Component
public class AwsS3Provider extends AbstractAwsProvider {
    private static Logger log = Logger.getLogger(AwsS3Provider.class.getName());

    private S3Client s3client;

    @PostConstruct
    private void setUpS3Client() {
        final Region region = Region.of(REGION_PROP);

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
