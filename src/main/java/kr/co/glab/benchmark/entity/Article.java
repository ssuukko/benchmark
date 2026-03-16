package kr.co.glab.benchmark.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(
        name = "articles",
        indexes = {
                @Index(name = "idx_articles_collected_at", columnList = "collected_at"),
                @Index(name = "idx_articles_source", columnList = "source")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_articles_source_external_id", columnNames = {"source", "external_id"})
        }
)
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "external_id", nullable = false, length = 100)
    private String externalId;

    @Column(nullable = false, length = 500)
    private String title;

    @Column(nullable = false, length = 1000)
    private String url;

    @Column(nullable = false, length = 50)
    private String source;

    @Column(nullable = false)
    private Integer score;

    @Column(name = "comment_count", nullable = false)
    private Integer commentCount;

    @Column(name = "collected_at", nullable = false)
    private LocalDateTime collectedAt;

    @Column(name = "published_at")
    private LocalDateTime publishedAt;

    @Lob
    @Column(name = "content_text")
    private String contentText;
}
