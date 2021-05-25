package com.me.aws;

import lombok.Getter;

@Getter
public enum CredentialsStateHolder {
    AWS_ACCESS_KEY_ID(""),
    AWS_SECRET_KEY(""),
    AWS_SNS_TOPIC_ARN(""),
    AWS_SQS_QUEUE_NAME("");

    private String value;

    CredentialsStateHolder(String value) {
        this.value = value;
    }

    public void setValue(String newValue) {
        this.value = newValue;
    }

    public static void setUp(final String[] args) {
        if (args.length > 3) {
            CredentialsStateHolder.AWS_ACCESS_KEY_ID.setValue(args[0]);
            CredentialsStateHolder.AWS_SECRET_KEY.setValue(args[1]);
            CredentialsStateHolder.AWS_SNS_TOPIC_ARN.setValue(args[2]);
            CredentialsStateHolder.AWS_SQS_QUEUE_NAME.setValue(args[3]);
        } else {
            throw new RuntimeException("Inappropriate amount of arguments.");
        }
    }
}
