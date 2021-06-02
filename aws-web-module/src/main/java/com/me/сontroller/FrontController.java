package com.me.сontroller;

import com.me.service.ScheduledService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import static com.me.aws.CredentialsStateHolder.AWS_ACCESS_KEY_ID;
import static com.me.aws.CredentialsStateHolder.AWS_SECRET_KEY;

@RestController
public class FrontController {
    private static Logger log = Logger.getLogger(FrontController.class.getName());
    private final ScheduledService scheduledService;

    @Autowired
    public FrontController(ScheduledService scheduledService) {
        this.scheduledService = scheduledService;
    }

    @GetMapping("/")
    public ResponseEntity<String> index() {
        log.info("FrontController#index()");
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Hello!<br/><br/>" +
                        "Click to check zone:<br/>" +
                        "<a href=\"/check-zone\">/Check zone</a><br/><br/>" +
                        "Click to subscribe to SNS:<br/>" +
                        "<a href=\"/users/1/subscribe-email?email=rohau.andrei@gmail.com\">/users/1/subscribe-email?email=rohau.andrei@gmail.com</a><br/><br/>" +
                        "Click to unsubscribe to SNS:<br/>" +
                        "<a href=\"/users/1/unsubscribe-email?email=rohau.andrei@gmail.com\">/users/1/unsubscribe-email?email=rohau.andrei@gmail.com</a><br/><br/>" +
                        "Click to view images metadata:<br/>" +
                        "<a href=\"/images\">/images</a><br/><br/>" +
                        "Click to view image random-metadata:<br/>" +
                        "<a href=\"/images/random-metadata\">/Random image</a><br/><br/>" +
                        "Click to schedule notifications:<br/>" +
                        "<a href=\"/run-scheduled-notification\">/Begin scheduled notifications</a><br/><br/>" +
                        "Click to publish notifications:<br/>" +
                        "<a href=\"/run-publish-notification\">/Publish notifications</a><br/><br/>" +
                        "Click to publish notifications:<br/>" +
                        "<a href=\"/run-lambda-publish-notification\">/Call lambda to publish notifications</a><br/><br/>");
    }

    @GetMapping("/run-scheduled-notification")
    public ResponseEntity<String> runScheduledNotification() {
        log.info("FrontController#runScheduledNotification()");
        scheduledService.runScheduledTask(scheduledService.prepareNotificationTask(), 10, 20, TimeUnit.SECONDS);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Notifications scheduled!<br/><br/> " +
                        "Click to go back home:<br/>" +
                        "<a href=\"/\">/Back home</a><br/><br/>");
    }

    @GetMapping("/run-publish-notification")
    public ResponseEntity<String> runPublishNotification() {
        log.info("FrontController#runPublishNotification()");
        scheduledService.prepareNotificationTask().run();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Notifications published!<br/><br/> " +
                        "Click to go back home:<br/>" +
                        "<a href=\"/\">/Back home</a><br/><br/>");
    }

    @GetMapping("/run-lambda-publish-notification")
    public ResponseEntity<String> runLambdaPublishNotification() {
        log.info("FrontController#runLambdaPublishNotification()");
        scheduledService.prepareLambdaNotificationTask().run();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Call lambda to run notifications!<br/><br/> " +
                        "Click to go back home:<br/>" +
                        "<a href=\"/\">/Back home</a><br/><br/>");
    }

    @GetMapping("/status")
    public ResponseEntity<String> status() {
        log.info("FrontController#status()");
        return ResponseEntity.ok("Success! <br/>" +
                "Arguments=[" + AWS_ACCESS_KEY_ID.getValue() + ", " + AWS_SECRET_KEY.getValue() + "]<br/><br/>" +
                "Click to go back home:<br/>" +
                "<a href=\"/\">/Back home</a><br/><br/>");
    }

    @GetMapping("/check-zone")
    public ResponseEntity<String> checkZone() {
        log.info("FrontController#checkZone()");
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Zone is [" + Calendar.getInstance().getTimeZone().getDisplayName() + "]<br/><br/>" +
                        "Click to go back home:<br/>" +
                        "<a href=\"/\">/Back home</a>");
    }
}
