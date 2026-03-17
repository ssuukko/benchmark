package kr.co.glab.benchmark.service;

import java.util.Map;
import kr.co.glab.benchmark.dto.HackerNewsItemDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HackerNewsItemEvaluatorImpl implements HackerNewsItemEvaluator {

    private final ArticleModelAnalyzer articleModelAnalyzer;

    @Override
    public HackerNewsItemEvaluation evaluate(HackerNewsItemDto item) {
        if (!isCollectable(item)) {
            return HackerNewsItemEvaluation.invalid(item);
        }

        String content = buildContent(item);
        Map<String, Integer> mentions = articleModelAnalyzer.analyze(content);
        if (!containsTrackedModel(mentions)) {
            return HackerNewsItemEvaluation.nonAi(item, content, mentions);
        }

        return HackerNewsItemEvaluation.matched(item, content, mentions);
    }

    private boolean isCollectable(HackerNewsItemDto item) {
        return item != null
                && item.id() != null
                && "story".equalsIgnoreCase(item.type())
                && !Boolean.TRUE.equals(item.dead())
                && !Boolean.TRUE.equals(item.deleted())
                && item.title() != null
                && !item.title().isBlank();
    }

    private String buildContent(HackerNewsItemDto item) {
        String title = item.title() == null ? "" : item.title();
        String text = item.text() == null ? "" : item.text().replaceAll("<[^>]*>", " ");
        return (title + " " + text).trim();
    }

    private boolean containsTrackedModel(Map<String, Integer> mentions) {
        return mentions.values().stream().anyMatch(count -> count > 0);
    }
}
