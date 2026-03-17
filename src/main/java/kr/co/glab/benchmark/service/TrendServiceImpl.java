package kr.co.glab.benchmark.service;

import java.util.List;
import kr.co.glab.benchmark.dto.TrendSummaryDto;
import kr.co.glab.benchmark.entity.TrendStat;
import kr.co.glab.benchmark.repository.TrendStatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TrendServiceImpl implements TrendService {

    private final TrendStatRepository trendStatRepository;
    private final WeekService weekService;

    @Override
    public List<TrendSummaryDto> getCurrentWeekSummary() {
        String week = currentWeek();
        return trendStatRepository.findByWeekOrderByMentionCountDesc(week)
                .stream()
                .map(this::toSummary)
                .toList();
    }

    @Override
    public String currentWeek() {
        return weekService.currentWeek();
    }

    private TrendSummaryDto toSummary(TrendStat stat) {
        double growthRate = stat.getPrevCount() == 0
                ? 100.0
                : ((double) (stat.getMentionCount() - stat.getPrevCount()) / stat.getPrevCount()) * 100.0;
        return new TrendSummaryDto(
                stat.getModelName(),
                stat.getWeek(),
                stat.getMentionCount(),
                stat.getPrevCount(),
                stat.getArticleCount(),
                growthRate
        );
    }
}
