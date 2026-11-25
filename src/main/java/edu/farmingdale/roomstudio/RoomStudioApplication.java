package edu.farmingdale.roomstudio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class RoomStudioApplication {
    public static void main(String[] args) {
        SpringApplication.run(RoomStudioApplication.class, args);
    }

    @Bean(destroyMethod = "shutdown")
    RoomEstimator roomEstimator() {
        return new RoomEstimator();
    }
}
