package kr.co.glab.benchmark.service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.WeekFields;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import kr.co.glab.benchmark.dto.HackerNewsCollectStatsDto;
import kr.co.glab.benchmark.dto.HackerNewsItemDto;
import kr.co.glab.benchmark.entity.Article;
import kr.co.glab.benchmark.entity.TrendStat;
import kr.co.glab.benchmark.repository.ArticleRepository;
import kr.co.glab.benchmark.repository.TrendStatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CollectServiceImpl implements CollectService {

    private static final String SOURCE_HACKER_NEWS = "HACKERNEWS";
    private static final int TOP_STORY_LIMIT = 30;

    private final HackerNewsClient hackerNewsClient;
    private final ArticleRepository articleRepository;
    private final TrendStatRepository trendStatRepository;

    @Override
    @Transactional
    public HackerNewsCollectStatsDto collectHackerNewsTopStories() {
        log.info("Starting Hacker News collection. topStoryLimit={}", TOP_STORY_LIMIT);
        List<Long> topStoryIds = hackerNewsClient.fetchTopStoryIds(TOP_STORY_LIMIT);
        List<HackerNewsItemDto> items = hackerNewsClient.fetchItems(topStoryIds);
        int savedCount = 0;
        int skippedCount = 0;
        int aiMatchedCount = 0;
        int duplicateCount = 0;
        int invalidCount = 0;

        for (HackerNewsItemDto item : items) {
            if (!isCollectable(item)) {
                log.debug("Skipping non-collectable HN item. storyId={}", item == null ? null : item.id());
                skippedCount++;
                invalidCount++;
                continue;
            }

            String content = buildContent(item);
            if (!containsAiKeyword(content)) {
                log.debug("Skipping non-AI HN item. storyId={} title={}", item.id(), item.title());
                skippedCount++;
                continue;
            }

            aiMatchedCount++;

            String externalId = String.valueOf(item.id());
            Optional<Article> existing = articleRepository.findBySourceAndExternalId(SOURCE_HACKER_NEWS, externalId);
            if (existing.isPresent()) {
                log.debug("Skipping duplicate HN item. storyId={} title={}", item.id(), item.title());
                skippedCount++;
                duplicateCount++;
                continue;
            }

            articleRepository.save(toArticle(item, externalId, content));
            log.info("Saved HN article. storyId={} score={} comments={} title={}",
                    item.id(), item.score(), item.descendants(), item.title());
            savedCount++;
        }

        refreshCurrentWeekStats();

        log.info(
                "Finished Hacker News collection. fetchedCount={} savedCount={} aiMatchedCount={} duplicateCount={} invalidCount={} skippedCount={}",
                topStoryIds.size(),
                savedCount,
                aiMatchedCount,
                duplicateCount,
                invalidCount,
                skippedCount
        );

        return new HackerNewsCollectStatsDto(
                topStoryIds.size(),
                savedCount,
                skippedCount,
                aiMatchedCount,
                duplicateCount,
                invalidCount
        );
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

    private Article toArticle(HackerNewsItemDto item, String externalId, String content) {
        Article article = new Article();
        article.setExternalId(externalId);
        article.setSource(SOURCE_HACKER_NEWS);
        article.setTitle(item.title());
        article.setUrl(item.url() == null || item.url().isBlank()
                ? "https://news.ycombinator.com/item?id=" + item.id()
                : item.url());
        article.setScore(item.score() == null ? 0 : item.score());
        article.setCommentCount(item.descendants() == null ? 0 : item.descendants());
        article.setCollectedAt(LocalDateTime.now());
        article.setPublishedAt(item.time() == null ? null : LocalDateTime.ofInstant(
                Instant.ofEpochSecond(item.time()),
                ZoneId.systemDefault()
        ));
        article.setContentText(content);
        return article;
    }

    private String buildContent(HackerNewsItemDto item) {
        String title = item.title() == null ? "" : item.title();
        String text = item.text() == null ? "" : item.text().replaceAll("<[^>]*>", " ");
        return (title + " " + text).trim();
    }

    private boolean containsAiKeyword(String content) {
        return modelMentions(content).values().stream().anyMatch(count -> count > 0);
    }

    private void refreshCurrentWeekStats() {
        String currentWeek = currentWeek();
        Map<String, Integer> currentCounts = calculateMentionCounts(weekRange(currentWeek));
        Map<String, Integer> previousCounts = calculateMentionCounts(weekRange(previousWeek()));
        int articleCount = articleRepository.findByCollectedAtBetween(weekStart(currentWeek), weekEnd(currentWeek)).size();

        trendStatRepository.deleteByWeek(currentWeek);

        for (String modelName : List.of("GPT", "CLAUDE", "GEMINI")) {
            TrendStat stat = new TrendStat();
            stat.setModelName(modelName);
            stat.setWeek(currentWeek);
            stat.setMentionCount(currentCounts.getOrDefault(modelName, 0));
            stat.setPrevCount(previousCounts.getOrDefault(modelName, 0));
            stat.setArticleCount(articleCount);
            trendStatRepository.save(stat);
        }
    }

    private Map<String, Integer> calculateMentionCounts(LocalDateTimeRange range) {
        Map<String, Integer> totals = new LinkedHashMap<>();
        totals.put("GPT", 0);
        totals.put("CLAUDE", 0);
        totals.put("GEMINI", 0);

        List<Article> articles = articleRepository.findByCollectedAtBetween(range.start(), range.end());
        for (Article article : articles) {
            Map<String, Integer> mentions = modelMentions(article.getContentText());
            for (Map.Entry<String, Integer> entry : mentions.entrySet()) {
                totals.computeIfPresent(entry.getKey(), (key, value) -> value + entry.getValue());
            }
        }
        return totals;
    }

    private Map<String, Integer> modelMentions(String content) {
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

    private String currentWeek() {
        return formatWeek(LocalDate.now());
    }

    private String previousWeek() {
        return formatWeek(LocalDate.now().minusWeeks(1));
    }

    private String formatWeek(LocalDate date) {
        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        int week = date.get(weekFields.weekOfWeekBasedYear());
        return "%d-W%02d".formatted(date.getYear(), week);
    }

    private LocalDateTimeRange weekRange(String week) {
        return new LocalDateTimeRange(weekStart(week), weekEnd(week));
    }

    private LocalDateTime weekStart(String week) {
        String[] parts = week.split("-W");
        int year = Integer.parseInt(parts[0]);
        int weekNumber = Integer.parseInt(parts[1]);
        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        LocalDate date = LocalDate.of(year, 1, 4)
                .with(weekFields.weekOfWeekBasedYear(), weekNumber)
                .with(weekFields.dayOfWeek(), 1);
        return date.atStartOfDay();
    }

    private LocalDateTime weekEnd(String week) {
        return weekStart(week).plusDays(7).minusNanos(1);
    }

    private record LocalDateTimeRange(LocalDateTime start, LocalDateTime end) {
    }
}
