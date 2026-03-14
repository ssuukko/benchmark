package kr.co.glab.benchmark.controller;

import kr.co.glab.benchmark.service.ArticleService;
import kr.co.glab.benchmark.service.TrendService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final ArticleService articleService;
    private final TrendService trendService;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("summary", trendService.getCurrentWeekSummary());
        model.addAttribute("recentArticles", articleService.getRecentArticles());
        model.addAttribute("currentWeek", trendService.currentWeek());
        return "home";
    }
}
