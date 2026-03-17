package kr.co.glab.benchmark.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import kr.co.glab.benchmark.dto.HackerNewsCollectStatsDto;
import kr.co.glab.benchmark.dto.HackerNewsItemDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CollectServiceImplTest {

    @Mock
    private HackerNewsClient hackerNewsClient;

    @Mock
    private HackerNewsItemEvaluator hackerNewsItemEvaluator;

    @Mock
    private HackerNewsItemProcessHandler processHandler;

    @Mock
    private TrendAggregationService trendAggregationService;

    private CollectServiceImpl collectService;

    @BeforeEach
    void setUp() {
        collectService = new CollectServiceImpl(
                hackerNewsClient,
                hackerNewsItemEvaluator,
                List.of(processHandler),
                trendAggregationService
        );
    }

    @Test
    void collectHackerNewsTopStoriesAggregatesHandlerStatuses() {
        HackerNewsItemDto invalidItem = item(1L);
        HackerNewsItemDto savedItem = item(2L);
        HackerNewsItemDto duplicateItem = item(3L);

        when(hackerNewsClient.fetchTopStoryIds(30)).thenReturn(List.of(1L, 2L, 3L));
        when(hackerNewsClient.fetchItems(List.of(1L, 2L, 3L))).thenReturn(List.of(invalidItem, savedItem, duplicateItem));
        when(hackerNewsItemEvaluator.evaluate(any(HackerNewsItemDto.class))).thenReturn(
                HackerNewsItemEvaluation.invalid(invalidItem),
                HackerNewsItemEvaluation.matched(savedItem, "saved", java.util.Map.of("GPT", 1)),
                HackerNewsItemEvaluation.matched(duplicateItem, "duplicate", java.util.Map.of("GPT", 1))
        );
        when(processHandler.supports(any(HackerNewsItemProcessContext.class))).thenReturn(true);
        when(processHandler.handle(any(HackerNewsItemProcessContext.class))).thenReturn(
                HackerNewsItemProcessStatus.INVALID,
                HackerNewsItemProcessStatus.SAVED,
                HackerNewsItemProcessStatus.DUPLICATE
        );
        doNothing().when(processHandler).log(any(HackerNewsItemProcessContext.class));

        HackerNewsCollectStatsDto stats = collectService.collectHackerNewsTopStories();

        assertThat(stats.fetchedCount()).isEqualTo(3);
        assertThat(stats.savedCount()).isEqualTo(1);
        assertThat(stats.skippedCount()).isEqualTo(2);
        assertThat(stats.aiMatchedCount()).isEqualTo(2);
        assertThat(stats.duplicateCount()).isEqualTo(1);
        assertThat(stats.invalidCount()).isEqualTo(1);
        verify(trendAggregationService).refreshCurrentWeekStats();
    }

    private HackerNewsItemDto item(Long id) {
        return new HackerNewsItemDto(id, "title-" + id, "https://example.com/" + id, 10, 3, 1L, "text", false, false, "story");
    }
}
