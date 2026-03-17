package kr.co.glab.benchmark.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class ArticleModelAnalyzerImpl implements ArticleModelAnalyzer {

    @Override
    public Map<String, Integer> analyze(String content) {
        String normalized = content == null ? "" : content.toLowerCase(Locale.ROOT);
        Map<String, Integer> mentions = new LinkedHashMap<>();
        mentions.put("GPT", keywordHitCount(normalized, List.of("gpt", "gpt-4", "gpt-4o", "chatgpt", "openai")));
        mentions.put("CLAUDE", keywordHitCount(normalized, List.of("claude", "anthropic", "claude-3", "sonnet", "opus")));
        mentions.put("GEMINI", keywordHitCount(normalized, List.of("gemini", "google ai", "bard", "gemini pro")));
        return mentions;
    }

    private int keywordHitCount(String content, List<String> keywords) {
        int count = 0;
        for (String keyword : keywords) {
            if (content.contains(keyword)) {
                count++;
            }
        }
        return count;
    }
}
