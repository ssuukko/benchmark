package kr.co.glab.benchmark.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "scheduler.hacker-news")
public record SchedulerProperties(
        boolean enabled,
        String cron,
        String zone
) {
}
