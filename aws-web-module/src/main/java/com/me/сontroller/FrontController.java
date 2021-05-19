package com.me.сontroller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Calendar;
import java.util.logging.Logger;

import static com.me.security.CredentialsStateHolder.AWS_ACCESS_KEY_ID;
import static com.me.security.CredentialsStateHolder.AWS_SECRET_KEY;

@RestController
public class FrontController {
    private static Logger log = Logger.getLogger(FrontController.class.getName());

    @GetMapping("/")
    public ResponseEntity<String> index() {
        log.info("FrontController#index()");
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Hello!<br/> " +
                        "Click:<br/>" +
                        "<a href=\"/check-zone\">/Check zone</a>");
    }

    @GetMapping("/status")
    public ResponseEntity<String> status() {
        log.info("FrontController#status()");
        return ResponseEntity.ok("Success! <br/> Arguments=[" + AWS_ACCESS_KEY_ID.getValue() + ", " + AWS_SECRET_KEY.getValue() + "]");
    }

    @GetMapping("/check-zone")
    public ResponseEntity<String> checkZone() {
        log.info("FrontController#checkZone()");
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Zone is [" + Calendar.getInstance().getTimeZone().getDisplayName() + "]<br/>" +
                        "<a href=\"/\">/Back home</a>");
    }
}
