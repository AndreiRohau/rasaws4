package com.me.aws.provider;

import com.me.aws.AbstractAwsProvider;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;

import javax.annotation.PostConstruct;
import java.util.logging.Logger;

@Component
public class AwsSnsProvider extends AbstractAwsProvider {
    private static Logger log = Logger.getLogger(AwsSnsProvider.class.getName());

    private SnsClient snsClient;

    @PostConstruct
    private void setUpSnsClient() {
        final Region region = Region.of(REGION_PROP);

        snsClient = SnsClient.builder()
                .credentialsProvider(credentialProvider)
                .region(region)
                .build();

        log.info("SnsClient is built. Region=" + region);
    }

    public SnsClient getSnsClient() {
        return snsClient;
    }
}
