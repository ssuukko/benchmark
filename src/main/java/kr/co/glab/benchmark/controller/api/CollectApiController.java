package kr.co.glab.benchmark.controller.api;

import kr.co.glab.benchmark.dto.HackerNewsCollectResponseDto;
import kr.co.glab.benchmark.service.ArticleService;
import kr.co.glab.benchmark.service.CollectService;
import kr.co.glab.benchmark.service.TrendService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/collect")
@RequiredArgsConstructor
public class CollectApiController {

    private final TrendService trendService;
    private final ArticleService articleService;
    private final CollectService collectService;

    @PostMapping("/hacker-news")
    public HackerNewsCollectResponseDto collectHackerNews(@RequestParam(defaultValue = "latest") String articleSort) {
        var stats = collectService.collectHackerNewsTopStories();
        return new HackerNewsCollectResponseDto(
                "Hacker News 수집 완료: %d건 조회, %d건 저장, %d건 AI 매칭, %d건 중복, %d건 무효, %d건 스킵".formatted(
                        stats.fetchedCount(),
                        stats.savedCount(),
                        stats.aiMatchedCount(),
                        stats.duplicateCount(),
                        stats.invalidCount(),
                        stats.skippedCount()
                ),
                stats,
                trendService.getCurrentWeekSummary(),
                articleService.getRecentArticles(articleSort)
        );
    }
}
