package kr.co.glab.benchmark;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BenchMarkApplication {

    public static void main(String[] args) {
        SpringApplication.run(BenchMarkApplication.class, args);
    }
}
