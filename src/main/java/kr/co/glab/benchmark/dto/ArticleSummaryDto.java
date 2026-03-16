package kr.co.glab.benchmark.dto;

import java.time.LocalDateTime;

public record ArticleSummaryDto(
        String source,
        Integer score,
        Integer commentCount,
        String url,
        String title,
        LocalDateTime collectedAt
) {
}
