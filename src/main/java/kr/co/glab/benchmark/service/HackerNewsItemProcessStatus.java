package kr.co.glab.benchmark.service;

public enum HackerNewsItemProcessStatus {
    INVALID(0, 1, 0, 0, 1),
    NON_AI(0, 1, 0, 0, 0),
    DUPLICATE(0, 1, 1, 1, 0),
    SAVED(1, 0, 1, 0, 0);

    private final int savedDelta;
    private final int skippedDelta;
    private final int aiMatchedDelta;
    private final int duplicateDelta;
    private final int invalidDelta;

    HackerNewsItemProcessStatus(
            int savedDelta,
            int skippedDelta,
            int aiMatchedDelta,
            int duplicateDelta,
            int invalidDelta
    ) {
        this.savedDelta = savedDelta;
        this.skippedDelta = skippedDelta;
        this.aiMatchedDelta = aiMatchedDelta;
        this.duplicateDelta = duplicateDelta;
        this.invalidDelta = invalidDelta;
    }

    public int savedDelta() {
        return savedDelta;
    }

    public int skippedDelta() {
        return skippedDelta;
    }

    public int aiMatchedDelta() {
        return aiMatchedDelta;
    }

    public int duplicateDelta() {
        return duplicateDelta;
    }

    public int invalidDelta() {
        return invalidDelta;
    }
}
