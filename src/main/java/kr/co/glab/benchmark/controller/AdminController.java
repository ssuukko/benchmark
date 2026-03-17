package kr.co.glab.benchmark.controller;

import kr.co.glab.benchmark.service.SchedulerSettingsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class AdminController {

    private final SchedulerSettingsService schedulerSettingsService;

    @GetMapping("/admin")
    public String adminRoot() {
        return "redirect:/admin/main";
    }

    @GetMapping("/admin/main")
    public String admin(Model model) {
        model.addAttribute("scheduler", schedulerSettingsService.getHackerNewsSettings());
        model.addAttribute("adminSection", "main");
        return "admin/index";
    }

    @GetMapping("/admin/schedule")
    public String schedule(Model model) {
        model.addAttribute("scheduler", schedulerSettingsService.getHackerNewsSettings());
        model.addAttribute("adminSection", "schedule");
        return "admin/schedule";
    }
}
