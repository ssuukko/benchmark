package kr.co.glab.benchmark.controller;

import java.util.List;
import kr.co.glab.benchmark.service.ArticleService;
import kr.co.glab.benchmark.service.SchedulerSettingsService;
import kr.co.glab.benchmark.service.TrendService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final ArticleService articleService;
    private final SchedulerSettingsService schedulerSettingsService;
    private final TrendService trendService;

    @GetMapping("/")
    public String home(@RequestParam(defaultValue = "latest") String articleSort, Model model) {
        var scheduler = schedulerSettingsService.getHackerNewsSettings();
        model.addAttribute("summary", trendService != null ? trendService.getCurrentWeekSummary() : List.of());
        model.addAttribute("recentArticles", articleService != null ? articleService.getRecentArticles(articleSort) : List.of());
        model.addAttribute("articleSort", articleSort);
        model.addAttribute("currentWeek", trendService != null ? trendService.currentWeek() : "집계 없음");
        model.addAttribute("schedulerEnabled", scheduler.enabled());
        model.addAttribute("schedulerTime", scheduler.scheduleSummary());
        model.addAttribute("schedulerZone", scheduler.zone());
        return "home";
    }
}
