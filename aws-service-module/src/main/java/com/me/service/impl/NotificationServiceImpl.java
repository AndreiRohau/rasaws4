package com.me.service.impl;

import com.me.aws.AwsClientFactory;
import com.me.domain.Image;
import com.me.service.NotificationService;
import com.me.service.dto.SqsMessageDto;
import com.me.service.enumType.EventEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.*;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.*;

import java.util.List;
import java.util.logging.Logger;

import static com.me.aws.AwsClientNameEnum.SNS;
import static com.me.aws.AwsClientNameEnum.SQS;
import static com.me.aws.CredentialsStateHolder.AWS_SNS_TOPIC_ARN;
import static com.me.aws.CredentialsStateHolder.AWS_SQS_QUEUE_NAME;
import static java.util.stream.Collectors.toList;

@Service
public class NotificationServiceImpl implements NotificationService {
    private static Logger log = Logger.getLogger(NotificationServiceImpl.class.getName());

    private final SnsClient snsClient;
    private final SqsClient sqsClient;

    @Autowired
    public NotificationServiceImpl(AwsClientFactory awsClientFactory) {
        this.snsClient = (SnsClient) awsClientFactory.getClients().get(SNS);
        this.sqsClient = (SqsClient) awsClientFactory.getClients().get(SQS);
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
            final ListSubscriptionsRequest request = ListSubscriptionsRequest.builder().build();
            final List<Subscription> subscriptions = snsClient.listSubscriptions(request).subscriptions();
            final Subscription subscription = subscriptions.stream()
                    .filter(s -> s.endpoint().equals(email)
                            && s.subscriptionArn().equals(AWS_SNS_TOPIC_ARN.getValue()))
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
        final List<String> messageTexts = sqsMessages.stream().map(Message::body).collect(toList());
        publishSnsTopic(messageTexts);
        deleteProcessedSqsMessages(sqsMessages, sqsUrl);
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
