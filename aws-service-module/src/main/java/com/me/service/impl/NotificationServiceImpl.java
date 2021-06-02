package com.me.service.impl;

import com.me.aws.AwsClientFactory;
import com.me.domain.Image;
import com.me.service.NotificationService;
import com.me.service.dto.SqsMessageDto;
import com.me.service.enumType.EventEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.lambda.LambdaClient;
import software.amazon.awssdk.services.lambda.model.InvokeRequest;
import software.amazon.awssdk.services.lambda.model.InvokeResponse;
import software.amazon.awssdk.services.lambda.model.LambdaException;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.*;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.*;

import java.util.List;
import java.util.logging.Logger;

import static com.me.aws.AwsClientNameEnum.*;
import static com.me.aws.CredentialsStateHolder.*;
import static java.util.stream.Collectors.toList;

@Service
public class NotificationServiceImpl implements NotificationService {
    private static Logger log = Logger.getLogger(NotificationServiceImpl.class.getName());

    private final SnsClient snsClient;
    private final SqsClient sqsClient;
    private final LambdaClient lambdaClient;

    @Value("${aws.region}")
    protected String REGION_PROP;

    @Autowired
    public NotificationServiceImpl(AwsClientFactory awsClientFactory) {
        this.snsClient = (SnsClient) awsClientFactory.getClients().get(SNS);
        this.sqsClient = (SqsClient) awsClientFactory.getClients().get(SQS);
        this.lambdaClient = (LambdaClient) awsClientFactory.getClients().get(LAMBDA);
    }

    // subscribe to SNS (arn passed as an arg) and send message that the user is subscribed
    @Override
    public void subscribeEmail(String userId, String email) {
        try {
            log.info("NotificationServiceImpl#subscribeEmail()");
            final SubscribeRequest subscribeRequest = SubscribeRequest.builder()
                    .protocol("email")
                    .endpoint(email)
                    .returnSubscriptionArn(true)
                    .topicArn(AWS_SNS_TOPIC_ARN.getValue())
                    .build();

            final SubscribeResponse result = snsClient.subscribe(subscribeRequest);
            log.info("User=[" + userId + "], with email=[" + email + "]. Subscribed. " +
                    "Subscription ARN: " + result.subscriptionArn() + ". Status was " + result.sdkHttpResponse().statusCode());
        } catch (SnsException e) {
            log.warning("Could not subscribe email=[" + email + "], " +
                    "to snsTopicArn=[" + AWS_SNS_TOPIC_ARN.getValue() + "].");
            throw new RuntimeException("Could not subscribe.");
        }
    }

    // Unsubscribe from SNS (arn passed as an arg)
    @Override
    public void unsubscribeEmail(String userId, final String email) {
        try {
            log.info("NotificationServiceImpl#unsubscribeEmail()");
            final ListSubscriptionsByTopicRequest request = ListSubscriptionsByTopicRequest.builder()
                    .topicArn(AWS_SNS_TOPIC_ARN.getValue())
                    .build();
            final List<Subscription> subscriptions = snsClient.listSubscriptionsByTopic(request).subscriptions();
            log.info(subscriptions.size() + " of " + subscriptions);

            final Subscription subscription = subscriptions.stream()
                    .filter(s -> s.endpoint().equals(email))
                    .findFirst()
                    .get();
            final UnsubscribeRequest unsubscribeRequest = UnsubscribeRequest.builder()
                    .subscriptionArn(subscription.subscriptionArn())
                    .build();

            final UnsubscribeResponse result = snsClient.unsubscribe(unsubscribeRequest);

            log.info("User=[" + userId + "], with email=[" + email + "]. Unsubscribed. " +
                    "Subscription was removed for [" + unsubscribeRequest.subscriptionArn() + "], " +
                    "Status was [" + result.sdkHttpResponse().statusCode() + "].");
        } catch (SnsException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }

    // put event to SQS
    @Override
    public void publishUploadImageEvent(EventEnum eventEnum, Image image, String resourceUrl) {
        log.info("NotificationServiceImpl#publishEvent()");
        final String queueUrl = getSqsUrl();

        final SendMessageRequest sendMessageRequest = SendMessageRequest.builder()
                .queueUrl(getSqsUrl())
                .messageBody((new SqsMessageDto(eventEnum, image, resourceUrl)).toString())
                .delaySeconds(5)
                .build();

        sqsClient.sendMessage(sendMessageRequest);
        log.info("EventEnum=[" + eventEnum + "], with resourceUrl=[" + resourceUrl + "], image=[" + image + "]. Is published to SQS. " +
                "Queue Url is=[" + queueUrl + "]");
    }

    // extract events from SQS and put them to SNS
    @Override
    public void sendNotifications() {
        log.info("NotificationServiceImpl#sendNotifications()");
        final String sqsUrl = getSqsUrl();
        final List<Message> sqsMessages = receiveSqsMessages(sqsUrl);
        log.info("NotificationServiceImpl#sendNotifications(), messages=[" + sqsMessages + "]");
        final List<String> messageTexts = sqsMessages.stream().map(Message::body).collect(toList());
        publishSnsTopic(messageTexts);
        deleteProcessedSqsMessages(sqsMessages, sqsUrl);
    }

    @Override
    public void callLambdaToSendNotifications() {
        log.info("NotificationServiceImpl#callLambdaToSendNotifications()");
        try {
            InvokeResponse res;
            //Need a SdkBytes instance for the payload
            String json = "{\n" +
                    "  \"msg\": \"Lambda got the message =)\",\n" +
                    "  \"awsAccessKeyId\": \"" + AWS_ACCESS_KEY_ID.getValue() + "\",\n" +
                    "  \"awsSecretKey\": \"" + AWS_SECRET_KEY.getValue() + "\",\n" +
                    "  \"awsRegion\": \"" + REGION_PROP + "\",\n" +
                    "  \"awsSnsTopicArn\": \"" + AWS_SNS_TOPIC_ARN.getValue() + "\",\n" +
                    "  \"awsSqsUrl\": \"https://sqs.us-east-1.amazonaws.com/716858514256/MyOwnQueue\",\n" +
                    "  \"awsSqsQueueName\": \"" + AWS_SQS_QUEUE_NAME.getValue() + "\"\n" +
                    "}";

            log.info("NotificationServiceImpl#callLambdaToSendNotifications(). Lambda payload=[" + json + "].");
            SdkBytes payload = SdkBytes.fromUtf8String(json) ;

            //Setup an InvokeRequest
            InvokeRequest request = InvokeRequest.builder()
                    .functionName(AWS_LAMBDA_FUNCTION_NAME.getValue())
                    .payload(payload)
                    .build();

            //Invoke the Lambda function
            res = lambdaClient.invoke(request);
            String value = res.payload().asUtf8String();
            log.info("NotificationServiceImpl#callLambdaToSendNotifications(). Lambda results=[" + value + "].");

        } catch(LambdaException e) {
            log.warning("NotificationServiceImpl#callLambdaToSendNotifications() thrown an error.");
//            log.warning(e.getMessage());
            e.printStackTrace();
        }
    }

    private String getSqsUrl() {
        log.info("NotificationServiceImpl#getSqsUrl()");
        final GetQueueUrlRequest getQueueRequest = GetQueueUrlRequest.builder()
                .queueName(AWS_SQS_QUEUE_NAME.getValue())
                .build();
        return sqsClient.getQueueUrl(getQueueRequest).queueUrl();
    }

    private List<Message> receiveSqsMessages(final String sqsUrl) {
        try {
            log.info("NotificationServiceImpl#receiveSqsMessages()");
            final ReceiveMessageRequest receiveMessageRequest = ReceiveMessageRequest.builder()
                    .queueUrl(sqsUrl)
                    .maxNumberOfMessages(10)
                    .waitTimeSeconds(20)
                    .build();
            return sqsClient.receiveMessage(receiveMessageRequest).messages();
        } catch (SqsException e) {
            log.warning("Exception during getting messages from SQS queue.");
            throw new RuntimeException("Exception during getting messages from SQS queue.", e);
        }
    }

    private void publishSnsTopic(final List<String> messages) {
        try {
            log.info("NotificationServiceImpl#publishSnsTopic()");
            for (final String message: messages) {
                final PublishRequest request = PublishRequest.builder()
                        .message(message)
                        .topicArn(AWS_SNS_TOPIC_ARN.getValue())
                        .build();
                final PublishResponse result = snsClient.publish(request);
            }
        } catch (SnsException e) {
            log.warning("Exception during publishing SNS Topic.");
            throw new RuntimeException("Exception during publishing SNS Topic.", e);
        }
    }

    private void deleteProcessedSqsMessages(List<Message> sqsMessages, final String sqsUrl) {
        log.info("NotificationServiceImpl#deleteProcessedSqsMessages()");
        for (Message sqsMessage : sqsMessages) {
            DeleteMessageRequest deleteMessageRequest = DeleteMessageRequest.builder()
                    .queueUrl(sqsUrl)
                    .receiptHandle(sqsMessage.receiptHandle())
                    .build();
            sqsClient.deleteMessage(deleteMessageRequest);
        }
    }
}
