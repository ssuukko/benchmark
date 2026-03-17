package kr.co.glab.benchmark.service;

public record SchedulerSettingsView(
        String schedulerName,
        boolean enabled,
        String cron,
        String zone
) {
}
