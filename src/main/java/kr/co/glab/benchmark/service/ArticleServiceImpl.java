package kr.co.glab.benchmark.service;

import java.util.List;
import kr.co.glab.benchmark.dto.ArticleSummaryDto;
import kr.co.glab.benchmark.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;

    @Override
    public List<ArticleSummaryDto> getRecentArticles() {
        return getRecentArticles("latest");
    }

    @Override
    public List<ArticleSummaryDto> getRecentArticles(String sort) {
        return articleRepository.findArticleSummaries(PageRequest.of(0, 5, toSort(sort)));
    }

    private Sort toSort(String sort) {
        if ("score".equalsIgnoreCase(sort)) {
            return Sort.by(
                    Sort.Order.desc("score"),
                    Sort.Order.desc("collectedAt")
            );
        }

        return Sort.by(Sort.Order.desc("collectedAt"));
    }
}
