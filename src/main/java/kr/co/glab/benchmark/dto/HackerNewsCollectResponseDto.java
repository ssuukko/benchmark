package kr.co.glab.benchmark.dto;

import java.util.List;

public record HackerNewsCollectResponseDto(
        String message,
        HackerNewsCollectStatsDto stats,
        List<TrendSummaryDto> summary,
        List<ArticleSummaryDto> recentArticles
) {
}
