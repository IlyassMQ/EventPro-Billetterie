package org.example.eventpro_billetterie;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EventProBilletterieApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(EventProBilletterieApiApplication.class, args);
    }

}
