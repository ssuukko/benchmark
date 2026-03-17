package kr.co.glab.benchmark.controller;

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
        model.addAttribute("summary", trendService.getCurrentWeekSummary());
        model.addAttribute("recentArticles", articleService.getRecentArticles(articleSort));
        model.addAttribute("articleSort", articleSort);
        model.addAttribute("currentWeek", trendService.currentWeek());
        model.addAttribute("schedulerEnabled", scheduler.enabled());
        model.addAttribute("schedulerCron", scheduler.cron());
        model.addAttribute("schedulerZone", scheduler.zone());
        return "home";
    }
}
