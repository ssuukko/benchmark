package kr.co.glab.benchmark.scheduler;

import kr.co.glab.benchmark.dto.HackerNewsCollectStatsDto;
import kr.co.glab.benchmark.service.CollectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class CollectScheduler {

    private final CollectService collectService;

    @Scheduled(cron = "${scheduler.hacker-news.cron:0 0 2 * * *}")
    public void collectHackerNews() {
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
