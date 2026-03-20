package kr.co.glab.benchmark.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import kr.co.glab.benchmark.entity.ArticleModel;
import kr.co.glab.benchmark.entity.TrendStat;
import kr.co.glab.benchmark.repository.ArticleModelRepository;
import kr.co.glab.benchmark.repository.ArticleRepository;
import kr.co.glab.benchmark.repository.TrendStatRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TrendAggregationServiceImplTest {

    @Mock
    private WeekService weekService;

    @Mock
    private ArticleRepository articleRepository;

    @Mock
    private ArticleModelRepository articleModelRepository;

    @Mock
    private TrendStatRepository trendStatRepository;

    @Captor
    private ArgumentCaptor<TrendStat> trendStatCaptor;

    private TrendAggregationServiceImpl trendAggregationService;

    @BeforeEach
    void setUp() {
        trendAggregationService = new TrendAggregationServiceImpl(
                weekService,
                articleRepository,
                articleModelRepository,
                trendStatRepository
        );
    }

    @Test
    void refreshCurrentWeekStatsUpdatesExistingAndCreatesMissingStats() {
        // given
        LocalDateTime currentStart = LocalDateTime.of(2026, 3, 16, 0, 0);
        LocalDateTime currentEnd = LocalDateTime.of(2026, 3, 22, 23, 59);
        LocalDateTime previousStart = LocalDateTime.of(2026, 3, 9, 0, 0);
        LocalDateTime previousEnd = LocalDateTime.of(2026, 3, 15, 23, 59);
        TrendStat existingGpt = new TrendStat();
        existingGpt.setId(1L);
        existingGpt.setWeek("2026-W12");
        existingGpt.setModelName("GPT");

        when(weekService.currentWeek()).thenReturn("2026-W12");
        when(weekService.previousWeek()).thenReturn("2026-W11");
        when(weekService.weekStart("2026-W12")).thenReturn(currentStart);
        when(weekService.weekEnd("2026-W12")).thenReturn(currentEnd);
        when(weekService.weekStart("2026-W11")).thenReturn(previousStart);
        when(weekService.weekEnd("2026-W11")).thenReturn(previousEnd);
        when(articleRepository.countByCollectedAtBetween(currentStart, currentEnd)).thenReturn(7L);
        when(articleModelRepository.findByArticleCollectedAtBetween(currentStart, currentEnd)).thenReturn(List.of(
                articleModel("GPT", 2),
                articleModel("GPT", 1),
                articleModel("CLAUDE", 4)
        ));
        when(articleModelRepository.findByArticleCollectedAtBetween(previousStart, previousEnd)).thenReturn(List.of(
                articleModel("GPT", 5),
                articleModel("GEMINI", 3)
        ));
        when(trendStatRepository.findByWeekAndModelName("2026-W12", "GPT")).thenReturn(Optional.of(existingGpt));
        when(trendStatRepository.findByWeekAndModelName("2026-W12", "CLAUDE")).thenReturn(Optional.empty());
        when(trendStatRepository.findByWeekAndModelName("2026-W12", "GEMINI")).thenReturn(Optional.empty());

        // when
        trendAggregationService.refreshCurrentWeekStats();

        // then
        verify(trendStatRepository).save(existingGpt);
        verify(trendStatRepository, org.mockito.Mockito.times(3)).save(trendStatCaptor.capture());
        List<TrendStat> savedStats = trendStatCaptor.getAllValues();

        TrendStat gpt = savedStats.stream().filter(stat -> "GPT".equals(stat.getModelName())).findFirst().orElseThrow();
        TrendStat claude = savedStats.stream().filter(stat -> "CLAUDE".equals(stat.getModelName())).findFirst().orElseThrow();
        TrendStat gemini = savedStats.stream().filter(stat -> "GEMINI".equals(stat.getModelName())).findFirst().orElseThrow();

        assertThat(gpt).isSameAs(existingGpt);
        assertThat(gpt.getMentionCount()).isEqualTo(3);
        assertThat(gpt.getPrevCount()).isEqualTo(5);
        assertThat(gpt.getArticleCount()).isEqualTo(7);

        assertThat(claude.getMentionCount()).isEqualTo(4);
        assertThat(claude.getPrevCount()).isZero();
        assertThat(claude.getArticleCount()).isEqualTo(7);

        assertThat(gemini.getMentionCount()).isZero();
        assertThat(gemini.getPrevCount()).isEqualTo(3);
        assertThat(gemini.getArticleCount()).isEqualTo(7);
    }

    private ArticleModel articleModel(String modelName, int mentionCount) {
        ArticleModel articleModel = new ArticleModel();
        articleModel.setModelName(modelName);
        articleModel.setMentionCount(mentionCount);
        return articleModel;
    }
}
