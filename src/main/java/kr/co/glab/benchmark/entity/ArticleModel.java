package kr.co.glab.benchmark.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(
        name = "article_models",
        indexes = {
                @Index(name = "idx_article_models_article_id", columnList = "article_id"),
                @Index(name = "idx_article_models_model_name", columnList = "model_name")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_article_models_article_model_name", columnNames = {"article_id", "model_name"})
        }
)
public class ArticleModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "article_id", nullable = false)
    private Article article;

    @Column(name = "model_name", nullable = false, length = 50)
    private String modelName;

    @Column(name = "mention_count", nullable = false)
    private Integer mentionCount;
}
