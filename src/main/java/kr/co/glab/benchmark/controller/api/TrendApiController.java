package kr.co.glab.benchmark.controller.api;

import kr.co.glab.benchmark.dto.ArticleSummaryDto;
import kr.co.glab.benchmark.service.ArticleService;
import kr.co.glab.benchmark.service.TrendService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TrendApiController {

    private final ArticleService articleService;
    private final TrendService trendService;

    @GetMapping("/trends/summary")
    public Object trendSummary() {
        return trendService.getCurrentWeekSummary();
    }

    @GetMapping("/articles")
    public List<ArticleSummaryDto> articles(@RequestParam(defaultValue = "latest") String sort) {
        return articleService.getRecentArticles(sort);
    }
}
