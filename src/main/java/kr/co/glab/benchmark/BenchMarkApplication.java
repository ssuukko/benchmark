package kr.co.glab.benchmark;

import kr.co.glab.benchmark.config.SchedulerProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties(SchedulerProperties.class)
public class BenchMarkApplication {

    public static void main(String[] args) {
        SpringApplication.run(BenchMarkApplication.class, args);
    }
}
