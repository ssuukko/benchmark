package kr.co.glab.benchmark.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import kr.co.glab.benchmark.dto.ArticleSummaryDto;
import kr.co.glab.benchmark.repository.ArticleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@ExtendWith(MockitoExtension.class)
class ArticleServiceImplTest {

    @Mock
    private ArticleRepository articleRepository;

    @Captor
    private ArgumentCaptor<Pageable> pageableCaptor;

    private ArticleServiceImpl articleService;

    @BeforeEach
    void setUp() {
        articleService = new ArticleServiceImpl(articleRepository);
    }

    @Test
    void getRecentArticlesUsesLatestSortByDefault() {
        // given
        List<ArticleSummaryDto> summaries = List.of(
                new ArticleSummaryDto("HACKERNEWS", 10, 2, "https://example.com", "title", LocalDateTime.now())
        );
        when(articleRepository.findArticleSummaries(any(Pageable.class))).thenReturn(summaries);

        // when
        List<ArticleSummaryDto> result = articleService.getRecentArticles();

        // then
        verify(articleRepository).findArticleSummaries(pageableCaptor.capture());
        Pageable pageable = pageableCaptor.getValue();

        assertThat(result).isEqualTo(summaries);
        assertThat(pageable.getPageNumber()).isZero();
        assertThat(pageable.getPageSize()).isEqualTo(5);
        assertThat(pageable.getSort()).isEqualTo(Sort.by(Sort.Order.desc("collectedAt")));
    }

    @Test
    void getRecentArticlesSortsByScoreWhenRequested() {
        // given
        when(articleRepository.findArticleSummaries(any(Pageable.class))).thenReturn(List.of());

        // when
        articleService.getRecentArticles("score");

        // then
        verify(articleRepository).findArticleSummaries(pageableCaptor.capture());
        Pageable pageable = pageableCaptor.getValue();

        assertThat(pageable.getSort()).isEqualTo(Sort.by(
                Sort.Order.desc("score"),
                Sort.Order.desc("collectedAt")
        ));
    }
}
