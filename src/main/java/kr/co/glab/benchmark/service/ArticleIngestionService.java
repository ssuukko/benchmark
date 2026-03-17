package kr.co.glab.benchmark.service;

import java.util.Map;
import kr.co.glab.benchmark.dto.HackerNewsItemDto;
import kr.co.glab.benchmark.entity.Article;

public interface ArticleIngestionService {

    Article saveHackerNewsArticle(HackerNewsItemDto item, String content, Map<String, Integer> mentions);
}
