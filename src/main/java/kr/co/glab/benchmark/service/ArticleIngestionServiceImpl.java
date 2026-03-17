package kr.co.glab.benchmark.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;
import kr.co.glab.benchmark.dto.HackerNewsItemDto;
import kr.co.glab.benchmark.entity.Article;
import kr.co.glab.benchmark.entity.ArticleModel;
import kr.co.glab.benchmark.repository.ArticleModelRepository;
import kr.co.glab.benchmark.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ArticleIngestionServiceImpl implements ArticleIngestionService {

    private static final String SOURCE_HACKER_NEWS = "HACKERNEWS";

    private final ArticleRepository articleRepository;
    private final ArticleModelRepository articleModelRepository;

    @Override
    public Article saveHackerNewsArticle(HackerNewsItemDto item, String content, Map<String, Integer> mentions) {
        Article article = articleRepository.save(toArticle(item, content));

        for (Map.Entry<String, Integer> entry : mentions.entrySet()) {
            if (entry.getValue() <= 0) {
                continue;
            }

            ArticleModel articleModel = new ArticleModel();
            articleModel.setArticle(article);
            articleModel.setModelName(entry.getKey());
            articleModel.setMentionCount(entry.getValue());
            articleModelRepository.save(articleModel);
        }

        return article;
    }

    private Article toArticle(HackerNewsItemDto item, String content) {
        Article article = new Article();
        article.setExternalId(String.valueOf(item.id()));
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
}
