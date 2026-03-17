package kr.co.glab.benchmark.scheduler;

import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import kr.co.glab.benchmark.dto.HackerNewsCollectStatsDto;
import kr.co.glab.benchmark.service.CollectService;
import kr.co.glab.benchmark.service.SchedulerSettingsService;
import kr.co.glab.benchmark.service.SchedulerSettingsServiceImpl;
import kr.co.glab.benchmark.service.SchedulerSettingsView;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class CollectSchedulingConfig implements SchedulingConfigurer {

    private final CollectService collectService;
    private final SchedulerSettingsService schedulerSettingsService;

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(1);
        taskScheduler.setThreadNamePrefix("hacker-news-scheduler-");
        taskScheduler.initialize();

        taskRegistrar.setTaskScheduler(taskScheduler);
        taskRegistrar.addTriggerTask(
                this::collectHackerNews,
                new Trigger() {
                    @Override
                    public Instant nextExecution(TriggerContext triggerContext) {
                        SchedulerSettingsView settings = schedulerSettingsService.getHackerNewsSettings();
                        if (!SchedulerSettingsServiceImpl.HACKER_NEWS_SCHEDULER.equals(settings.schedulerName())) {
                            return Instant.now().plus(1, ChronoUnit.MINUTES);
                        }
                        if (!settings.enabled()) {
                            return Instant.now().plus(1, ChronoUnit.MINUTES);
                        }

                        CronTrigger cronTrigger = new CronTrigger(settings.cron(), ZoneId.of(settings.zone()));
                        return cronTrigger.nextExecution(triggerContext);
                    }
                }
        );
    }

    private void collectHackerNews() {
        SchedulerSettingsView settings = schedulerSettingsService.getHackerNewsSettings();
        if (!settings.enabled()) {
            log.info("Scheduled Hacker News collection skipped because scheduler is disabled");
            return;
        }

        HackerNewsCollectStatsDto stats = collectService.collectHackerNewsTopStories();
        log.info(
                "Scheduled Hacker News collection finished. fetchedCount={} savedCount={} aiMatchedCount={} duplicateCount={} invalidCount={} skippedCount={}",
                stats.fetchedCount(),
                stats.savedCount(),
                stats.aiMatchedCount(),
                stats.duplicateCount(),
                stats.invalidCount(),
                stats.skippedCount()
        );
    }
}
