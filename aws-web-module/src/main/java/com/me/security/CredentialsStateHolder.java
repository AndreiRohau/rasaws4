package com.me.security;

import lombok.Getter;
@Getter
public enum CredentialsStateHolder {
    AWS_ACCESS_KEY_ID(""), AWS_SECRET_KEY("");

    private String value;

    CredentialsStateHolder(String value) {
        this.value = value;
    }

    public void setValue(String newValue) {
        this.value = newValue;
    }

    public static void setUp(final String[] args) {
        if (args.length == 2) {
            CredentialsStateHolder.AWS_ACCESS_KEY_ID.setValue(args[0]);
            CredentialsStateHolder.AWS_SECRET_KEY.setValue(args[1]);
        }
    }
}
