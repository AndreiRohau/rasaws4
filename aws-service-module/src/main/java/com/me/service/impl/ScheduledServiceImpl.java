package com.me.service.impl;

import com.me.service.NotificationService;
import com.me.service.ScheduledService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

@Component
public class ScheduledServiceImpl implements ScheduledService {
    private static Logger log = Logger.getLogger(ScheduledServiceImpl.class.getName());
    private final NotificationService notificationService;

    @Autowired
    public ScheduledServiceImpl(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    public void runScheduledTask(Runnable runnable, long initDelay, long period, TimeUnit timeUnit) {
        log.info("ScheduledServiceImpl#runScheduledTask(), run=" + runnable);
        final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(runnable, initDelay, period, timeUnit);
    }

    public Runnable prepareNotificationTask() {
        return () -> {
            log.info("Starting getting notifications...");
            notificationService.sendNotifications();
        };
    }
}