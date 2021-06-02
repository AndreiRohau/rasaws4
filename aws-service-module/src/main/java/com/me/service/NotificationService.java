package com.me.service;

import com.me.domain.Image;
import com.me.service.enumType.EventEnum;

public interface NotificationService {
    void subscribeEmail(String userId, String email);
    void unsubscribeEmail(String userId, String email);
    void publishUploadImageEvent(EventEnum eventEnum, Image image, String resourceUrl);
    void sendNotifications();
    void callLambdaToSendNotifications();
}
