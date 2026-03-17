package kr.co.glab.benchmark.service;

import java.util.List;
import kr.co.glab.benchmark.dto.HackerNewsCollectStatsDto;
import kr.co.glab.benchmark.dto.HackerNewsItemDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CollectServiceImpl implements CollectService {

    private static final int TOP_STORY_LIMIT = 30;

    private final HackerNewsClient hackerNewsClient;
    private final HackerNewsItemEvaluator hackerNewsItemEvaluator;
    private final List<HackerNewsItemProcessHandler> processHandlers;
    private final TrendAggregationService trendAggregationService;

    @Override
    @Transactional
    public HackerNewsCollectStatsDto collectHackerNewsTopStories() {
        log.info("Starting Hacker News collection. topStoryLimit={}", TOP_STORY_LIMIT);
        List<Long> topStoryIds = hackerNewsClient.fetchTopStoryIds(TOP_STORY_LIMIT);
        List<HackerNewsItemDto> items = hackerNewsClient.fetchItems(topStoryIds);
        HackerNewsCollectAccumulator accumulator = new HackerNewsCollectAccumulator();

        for (HackerNewsItemDto item : items) {
            processItem(item, accumulator);
        }

        trendAggregationService.refreshCurrentWeekStats();
        HackerNewsCollectStatsDto stats = accumulator.toStats(topStoryIds.size());

        log.info(
                "Finished Hacker News collection. fetchedCount={} savedCount={} aiMatchedCount={} duplicateCount={} invalidCount={} skippedCount={}",
                topStoryIds.size(),
                accumulator.savedCount(),
                accumulator.aiMatchedCount(),
                accumulator.duplicateCount(),
                accumulator.invalidCount(),
                accumulator.skippedCount()
        );

        return stats;
    }

    private void processItem(HackerNewsItemDto item, HackerNewsCollectAccumulator accumulator) {
        HackerNewsItemEvaluation evaluation = hackerNewsItemEvaluator.evaluate(item);
        HackerNewsItemProcessContext context = new HackerNewsItemProcessContext(item, evaluation);
        HackerNewsItemProcessHandler handler = processHandlers.stream()
                .filter(processHandler -> processHandler.supports(context))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No Hacker News item process handler found"));
        HackerNewsItemProcessStatus status = handler.handle(context);
        handler.log(context);
        accumulator.add(status);
    }
}
