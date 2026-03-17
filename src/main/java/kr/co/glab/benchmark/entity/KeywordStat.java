package kr.co.glab.benchmark.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
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
        name = "keyword_stats",
        indexes = {
                @Index(name = "idx_keyword_stats_week", columnList = "week"),
                @Index(name = "idx_keyword_stats_model_name", columnList = "model_name"),
                @Index(name = "idx_keyword_stats_keyword", columnList = "keyword")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_keyword_stats_week_model_name_keyword",
                        columnNames = {"week", "model_name", "keyword"})
        }
)
public class KeywordStat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String keyword;

    @Column(name = "model_name", nullable = false, length = 50)
    private String modelName;

    @Column(nullable = false, length = 10)
    private String week;

    @Column(nullable = false)
    private Integer count;
}
