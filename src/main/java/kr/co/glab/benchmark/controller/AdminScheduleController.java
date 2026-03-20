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
            @RequestParam String meridiem,
            @RequestParam int hour,
            @RequestParam int minute,
            @RequestParam int second,
            @RequestParam String zone,
            RedirectAttributes redirectAttributes
    ) {
        try {
            schedulerSettingsService.updateHackerNewsSettings(enabled, buildDailyCron(meridiem, hour, minute, second), zone);
            redirectAttributes.addFlashAttribute("message", "스케줄 설정을 저장했습니다.");
        } catch (IllegalArgumentException exception) {
            redirectAttributes.addFlashAttribute("error", "오전/오후, 시, 분, 초 값을 다시 확인해 주세요.");
        }

        return "redirect:/admin/schedule";
    }

    private String buildDailyCron(String meridiem, int hour, int minute, int second) {
        validateMeridiem(meridiem);
        validateRange(hour, 1, 12, "hour");
        validateRange(minute, 0, 59, "minute");
        validateRange(second, 0, 59, "second");
        return "%d %d %d * * *".formatted(second, minute, to24Hour(meridiem, hour));
    }

    private void validateRange(int value, int min, int max, String fieldName) {
        if (value < min || value > max) {
            throw new IllegalArgumentException(fieldName + " out of range");
        }
    }

    private void validateMeridiem(String meridiem) {
        if (!"AM".equals(meridiem) && !"PM".equals(meridiem)) {
            throw new IllegalArgumentException("meridiem out of range");
        }
    }

    private int to24Hour(String meridiem, int hour) {
        if ("AM".equals(meridiem)) {
            return hour == 12 ? 0 : hour;
        }

        return hour == 12 ? 12 : hour + 12;
    }
}
