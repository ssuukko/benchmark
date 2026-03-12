package kr.co.glab.benchmark.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "trend_stats")
public class TrendStat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "model_name", nullable = false, length = 50)
    private String modelName;

    @Column(nullable = false, length = 10)
    private String week;

    @Column(name = "mention_count", nullable = false)
    private Integer mentionCount;

    @Column(name = "prev_count", nullable = false)
    private Integer prevCount;

    @Column(name = "article_count", nullable = false)
    private Integer articleCount;
}
