package com.me.aws;

import com.me.aws.provider.AwsLambdaProvider;
import com.me.aws.provider.AwsS3Provider;
import com.me.aws.provider.AwsSnsProvider;
import com.me.aws.provider.AwsSqsProvider;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.SdkClient;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.HashMap;
import java.util.Map;

import static com.me.aws.AwsClientNameEnum.*;

@Component
public class AwsClientFactory {
    @Getter
    private final Map<AwsClientNameEnum, SdkClient> clients = new HashMap<>();
    private AwsS3Provider awsS3Provider;
    private AwsSnsProvider awsSnsProvider;
    private AwsSqsProvider awsSqsProvider;
    private AwsLambdaProvider awsLambdaProvider;

    @Autowired
    public AwsClientFactory(AwsS3Provider awsS3Provider, AwsSnsProvider awsSnsProvider, AwsSqsProvider awsSqsProvider, AwsLambdaProvider awsLambdaProvider) {
        this.awsS3Provider = awsS3Provider;
        this.awsSnsProvider = awsSnsProvider;
        this.awsSqsProvider = awsSqsProvider;
        this.awsLambdaProvider = awsLambdaProvider;
    }

    @PostConstruct
    private void setUp() {
        clients.put(S3, awsS3Provider.getS3client());
        clients.put(SNS, awsSnsProvider.getSnsClient());
        clients.put(SQS, awsSqsProvider.getSqsClient());
        clients.put(LAMBDA, awsLambdaProvider.getLambdaClient());
    }

    @PreDestroy
    private void tearDown() {
        for (AwsClientNameEnum clientName: clients.keySet()) {
            clients.get(clientName).close();
        }
    }
}
