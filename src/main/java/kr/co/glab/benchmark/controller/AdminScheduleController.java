package kr.co.glab.benchmark.controller;

import kr.co.glab.benchmark.service.SchedulerSettingsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class AdminScheduleController {

    private final SchedulerSettingsService schedulerSettingsService;

    @PostMapping("/admin/schedule")
    public String updateSchedule(
            @RequestParam(defaultValue = "false") boolean enabled,
            @RequestParam String cron,
            @RequestParam String zone,
            RedirectAttributes redirectAttributes
    ) {
        try {
            schedulerSettingsService.updateHackerNewsSettings(enabled, cron, zone);
            redirectAttributes.addFlashAttribute("message", "스케줄 설정을 저장했습니다.");
        } catch (IllegalArgumentException exception) {
            redirectAttributes.addFlashAttribute("error", "유효한 cron 표현식이 아닙니다.");
        }

        return "redirect:/admin/schedule";
    }
}
