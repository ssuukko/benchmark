package kr.co.glab.benchmark.service;

import kr.co.glab.benchmark.dto.HackerNewsCollectStatsDto;

public class HackerNewsCollectAccumulator {

    private int savedCount;
    private int skippedCount;
    private int aiMatchedCount;
    private int duplicateCount;
    private int invalidCount;

    public void add(HackerNewsItemProcessStatus status) {
        savedCount += status.savedDelta();
        skippedCount += status.skippedDelta();
        aiMatchedCount += status.aiMatchedDelta();
        duplicateCount += status.duplicateDelta();
        invalidCount += status.invalidDelta();
    }

    public HackerNewsCollectStatsDto toStats(int fetchedCount) {
        return new HackerNewsCollectStatsDto(
                fetchedCount,
                savedCount,
                skippedCount,
                aiMatchedCount,
                duplicateCount,
                invalidCount
        );
    }

    public int savedCount() {
        return savedCount;
    }

    public int skippedCount() {
        return skippedCount;
    }

    public int aiMatchedCount() {
        return aiMatchedCount;
    }

    public int duplicateCount() {
        return duplicateCount;
    }

    public int invalidCount() {
        return invalidCount;
    }
}
