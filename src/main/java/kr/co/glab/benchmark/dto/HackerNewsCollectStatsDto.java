package kr.co.glab.benchmark.dto;

public record HackerNewsCollectStatsDto(
        int fetchedCount,
        int savedCount,
        int skippedCount,
        int aiMatchedCount,
        int duplicateCount,
        int invalidCount
) {
}
