package com.me.aws.provider;

import com.me.aws.AbstractAwsProvider;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.lambda.LambdaClient;

import javax.annotation.PostConstruct;
import java.util.logging.Logger;

@Component
public class AwsLambdaProvider extends AbstractAwsProvider {
    private static Logger log = Logger.getLogger(AwsLambdaProvider.class.getName());

    private LambdaClient lambdaClient;

    @PostConstruct
    private void setUpSqsClient() {
        final Region region = Region.of(REGION_PROP);

        lambdaClient = LambdaClient.builder()
                .credentialsProvider(credentialProvider)
                .region(region)
                .build();

        log.info("LambdaClient is built. Region=" + region);
    }

    public LambdaClient getLambdaClient() {
        return lambdaClient;
    }
}
