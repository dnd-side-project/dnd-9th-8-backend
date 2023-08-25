package site.packit.packit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class PackItApplication {

    public static void main(String[] args) {
        SpringApplication.run(PackItApplication.class, args);
    }

}