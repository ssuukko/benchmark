package kr.co.glab.benchmark.service;

import java.util.List;
import kr.co.glab.benchmark.dto.ArticleSummaryDto;
import kr.co.glab.benchmark.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;

    @Override
    public List<ArticleSummaryDto> getRecentArticles() {
        return articleRepository.findTop5ByOrderByCollectedAtDesc()
                .stream()
                .map(article -> new ArticleSummaryDto(
                        article.getSource(),
                        article.getScore(),
                        article.getCommentCount(),
                        article.getUrl(),
                        article.getTitle(),
                        article.getCollectedAt()
                ))
                .toList();
    }
}
