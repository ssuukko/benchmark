package kr.co.glab.benchmark.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import kr.co.glab.benchmark.dto.ArticleSummaryDto;
import kr.co.glab.benchmark.entity.Article;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ArticleRepository extends JpaRepository<Article, Long> {

    Optional<Article> findBySourceAndExternalId(String source, String externalId);

    @Query("""
            select new kr.co.glab.benchmark.dto.ArticleSummaryDto(
                article.source,
                article.score,
                article.commentCount,
                article.url,
                article.title,
                article.collectedAt
            )
            from Article article
            """)
    List<ArticleSummaryDto> findArticleSummaries(Pageable pageable);

    List<Article> findByCollectedAtBetween(LocalDateTime start, LocalDateTime end);

    long countByCollectedAtBetween(LocalDateTime start, LocalDateTime end);
}
