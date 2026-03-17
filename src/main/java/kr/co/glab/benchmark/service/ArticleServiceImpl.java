package kr.co.glab.benchmark.service;

import java.util.List;
import kr.co.glab.benchmark.dto.ArticleSummaryDto;
import kr.co.glab.benchmark.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;

    @Override
    public List<ArticleSummaryDto> getRecentArticles() {
        return articleRepository.findArticleSummariesOrderByCollectedAtDesc(PageRequest.of(0, 5));
    }
}
