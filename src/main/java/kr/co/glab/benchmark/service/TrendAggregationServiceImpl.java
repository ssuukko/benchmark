package kr.co.glab.benchmark.service;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import kr.co.glab.benchmark.entity.ArticleModel;
import kr.co.glab.benchmark.entity.TrendStat;
import kr.co.glab.benchmark.repository.ArticleModelRepository;
import kr.co.glab.benchmark.repository.ArticleRepository;
import kr.co.glab.benchmark.repository.TrendStatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TrendAggregationServiceImpl implements TrendAggregationService {

    private static final List<String> TRACKED_MODELS = List.of("GPT", "CLAUDE", "GEMINI");

    private final WeekService weekService;
    private final ArticleRepository articleRepository;
    private final ArticleModelRepository articleModelRepository;
    private final TrendStatRepository trendStatRepository;

    @Override
    @Transactional
    public void refreshCurrentWeekStats() {
        String currentWeek = weekService.currentWeek();
        String previousWeek = weekService.previousWeek();
        Map<String, Integer> currentCounts = calculateMentionCounts(currentWeek);
        Map<String, Integer> previousCounts = calculateMentionCounts(previousWeek);
        int articleCount = articleRepository.findByCollectedAtBetween(
                weekService.weekStart(currentWeek),
                weekService.weekEnd(currentWeek)
        ).size();

        for (String modelName : TRACKED_MODELS) {
            TrendStat stat = trendStatRepository.findByWeekAndModelName(currentWeek, modelName)
                    .orElseGet(TrendStat::new);
            stat.setModelName(modelName);
            stat.setWeek(currentWeek);
            stat.setMentionCount(currentCounts.getOrDefault(modelName, 0));
            stat.setPrevCount(previousCounts.getOrDefault(modelName, 0));
            stat.setArticleCount(articleCount);
            trendStatRepository.save(stat);
        }
    }

    private Map<String, Integer> calculateMentionCounts(String week) {
        LocalDateTime start = weekService.weekStart(week);
        LocalDateTime end = weekService.weekEnd(week);
        Map<String, Integer> totals = emptyMentionMap();

        for (ArticleModel articleModel : articleModelRepository.findByArticleCollectedAtBetween(start, end)) {
            totals.computeIfPresent(
                    articleModel.getModelName(),
                    (key, value) -> value + articleModel.getMentionCount()
            );
        }

        return totals;
    }

    private Map<String, Integer> emptyMentionMap() {
        Map<String, Integer> totals = new LinkedHashMap<>();
        for (String modelName : TRACKED_MODELS) {
            totals.put(modelName, 0);
        }
        return totals;
    }
}
