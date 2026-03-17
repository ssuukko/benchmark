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
        name = "weekly_reports",
        indexes = {
                @Index(name = "idx_weekly_reports_week", columnList = "week")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_weekly_reports_week", columnNames = {"week"})
        }
)
public class WeeklyReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 10)
    private String week;

    @Column(name = "top_model_name", nullable = false, length = 50)
    private String topModelName;

    @Lob
    @Column(nullable = false)
    private String summary;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}
