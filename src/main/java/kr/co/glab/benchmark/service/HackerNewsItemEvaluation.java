package kr.co.glab.benchmark.service;

import java.util.Map;
import kr.co.glab.benchmark.dto.HackerNewsItemDto;

public record HackerNewsItemEvaluation(
        HackerNewsItemDto item,
        boolean collectable,
        boolean aiMatched,
        String content,
        Map<String, Integer> mentions
) {

    public static HackerNewsItemEvaluation invalid(HackerNewsItemDto item) {
        return new HackerNewsItemEvaluation(item, false, false, "", Map.of());
    }

    public static HackerNewsItemEvaluation nonAi(HackerNewsItemDto item, String content, Map<String, Integer> mentions) {
        return new HackerNewsItemEvaluation(item, true, false, content, mentions);
    }

    public static HackerNewsItemEvaluation matched(HackerNewsItemDto item, String content, Map<String, Integer> mentions) {
        return new HackerNewsItemEvaluation(item, true, true, content, mentions);
    }
}
