package com.me.сontroller;

import com.me.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

@RestController
@RequestMapping("/users")
public class UserController {
    private static Logger log = Logger.getLogger(UserController.class.getName());
    private final NotificationService notificationService;

    @Autowired
    public UserController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/{userId}/subscribe-email")
    public void subscribeEmail(@PathVariable String userId, @RequestParam String email) {
        log.info("UserController#subscribeEmail(), path=[" + userId + "], attributes=[" + email + "]");
        notificationService.subscribeEmail(userId, email);
    }

    @GetMapping("/{userId}/unsubscribe-email")
    public void unsubscribeEmail(@PathVariable String userId, @RequestParam String email) {
        log.info("UserController#unsubscribeEmail(), path=[" + userId + "], attributes=[" + email + "]");
        notificationService.unsubscribeEmail(userId, email);
    }
}
