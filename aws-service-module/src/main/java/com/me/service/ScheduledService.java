package com.me.service;

import java.util.concurrent.TimeUnit;

public interface ScheduledService {
    void runScheduledTask(Runnable runnable, long initDelay, long period, TimeUnit timeUnit);
    Runnable prepareNotificationTask();
    Runnable prepareLambdaNotificationTask();
}
