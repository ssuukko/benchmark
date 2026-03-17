package kr.co.glab.benchmark.service;

public interface SchedulerSettingsService {

    SchedulerSettingsView getHackerNewsSettings();

    SchedulerSettingsView updateHackerNewsSettings(boolean enabled, String cron, String zone);
}
