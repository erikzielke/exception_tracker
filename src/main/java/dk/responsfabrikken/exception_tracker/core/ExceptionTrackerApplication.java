package dk.responsfabrikken.exception_tracker.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ExceptionTrackerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExceptionTrackerApplication.class, args);
    }



}
