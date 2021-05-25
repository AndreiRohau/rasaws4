package com.me.aws.provider;

import com.me.aws.AbstractAwsProvider;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;

import javax.annotation.PostConstruct;
import java.util.logging.Logger;

@Component
public class AwsSqsProvider extends AbstractAwsProvider {
    private static Logger log = Logger.getLogger(AwsSqsProvider.class.getName());

    private SqsClient sqsClient;

    @PostConstruct
    private void setUpSqsClient() {
        final Region region = Region.of(REGION_PROP);

        sqsClient = SqsClient.builder()
                .credentialsProvider(credentialProvider)
                .region(region)
                .build();

        log.info("SqsClient is built. Region=" + region);
    }

    public SqsClient getSqsClient() {
        return sqsClient;
    }
}
