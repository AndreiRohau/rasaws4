package com.epam.сontroller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Calendar;

@RestController
public class FrontController {

    @GetMapping("/")
    public ResponseEntity<String> index() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Hello!<br/> " +
                        "Click:<br/>" +
                        "<a href=\"/check-zone\">/Check zone</a>");
    }

    @GetMapping("/check-zone")
    public ResponseEntity<String> checkZone() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Zone is [" + Calendar.getInstance().getTimeZone().getDisplayName() + "]<br/>" +
                        "<a href=\"/\">/Back home</a>");
    }
}
