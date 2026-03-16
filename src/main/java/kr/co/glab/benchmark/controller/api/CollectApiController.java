package kr.co.glab.benchmark.controller.api;

import kr.co.glab.benchmark.dto.HackerNewsCollectResponseDto;
import kr.co.glab.benchmark.service.ArticleService;
import kr.co.glab.benchmark.service.CollectService;
import kr.co.glab.benchmark.service.TrendService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
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
    public HackerNewsCollectResponseDto collectHackerNews() {
        var stats = collectService.collectHackerNewsTopStories();
        return new HackerNewsCollectResponseDto(
                "Hacker News 수집과 이번 주 집계를 반영했습니다.",
                stats,
                trendService.getCurrentWeekSummary(),
                articleService.getRecentArticles()
        );
    }
}
