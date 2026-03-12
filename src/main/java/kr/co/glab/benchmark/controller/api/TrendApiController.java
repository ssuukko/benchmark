package kr.co.glab.benchmark.controller.api;

import kr.co.glab.benchmark.service.TrendService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TrendApiController {

    private final TrendService trendService;

    @GetMapping("/trends/summary")
    public Object trendSummary() {
        return trendService.getCurrentWeekSummary();
    }
}
