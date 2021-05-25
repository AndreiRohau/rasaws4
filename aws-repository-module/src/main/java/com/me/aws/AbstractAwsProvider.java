package com.me.aws;

import org.springframework.beans.factory.annotation.Value;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;

import static com.me.aws.CredentialsStateHolder.AWS_ACCESS_KEY_ID;
import static com.me.aws.CredentialsStateHolder.AWS_SECRET_KEY;

public abstract class AbstractAwsProvider {
    @Value("${aws.region}")
    protected String REGION_PROP;
    protected AwsBasicCredentials credentials = AwsBasicCredentials.create(
            AWS_ACCESS_KEY_ID.getValue(),
            AWS_SECRET_KEY.getValue());
    protected AwsCredentialsProvider credentialProvider = () -> credentials;

}
