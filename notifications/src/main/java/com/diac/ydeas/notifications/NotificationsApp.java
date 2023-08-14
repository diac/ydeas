package com.diac.ydeas.notifications;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class NotificationsApp extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(NotificationsApp.class, args);
    }
}