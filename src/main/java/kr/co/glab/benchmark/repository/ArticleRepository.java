package kr.co.glab.benchmark.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import kr.co.glab.benchmark.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {

    Optional<Article> findBySourceAndExternalId(String source, String externalId);

    List<Article> findTop5ByOrderByCollectedAtDesc();

    List<Article> findByCollectedAtBetween(LocalDateTime start, LocalDateTime end);
}
