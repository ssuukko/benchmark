package kr.co.glab.benchmark.service;

import kr.co.glab.benchmark.dto.TrendSummaryDto;
import java.util.List;

public interface TrendService {

    List<TrendSummaryDto> getCurrentWeekSummary();

    String currentWeek();
}
