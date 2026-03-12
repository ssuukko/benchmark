package kr.co.glab.benchmark.dto;

public record TrendSummaryDto(
        String modelName,
        String week,
        int mentionCount,
        int prevCount,
        int articleCount,
        double growthRate
) {
}
