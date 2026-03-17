package kr.co.glab.benchmark.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import kr.co.glab.benchmark.entity.Article;
import kr.co.glab.benchmark.entity.ArticleModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ArticleModelRepository extends JpaRepository<ArticleModel, Long> {

    List<ArticleModel> findByModelNameOrderByMentionCountDesc(String modelName);

    List<ArticleModel> findByArticle(Article article);

    Optional<ArticleModel> findByArticleAndModelName(Article article, String modelName);

    @Query("""
            select articleModel
            from ArticleModel articleModel
            join fetch articleModel.article article
            where article.collectedAt between :start and :end
            """)
    List<ArticleModel> findByArticleCollectedAtBetween(LocalDateTime start, LocalDateTime end);
}
