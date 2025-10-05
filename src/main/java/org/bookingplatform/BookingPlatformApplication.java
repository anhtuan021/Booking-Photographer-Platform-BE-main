package org.bookingplatform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class BookingPlatformApplication {
    public static void main(String[] args) {
        SpringApplication.run(BookingPlatformApplication.class, args);
    }
}
