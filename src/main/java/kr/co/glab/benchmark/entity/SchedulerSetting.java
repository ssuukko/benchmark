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
        name = "scheduler_settings",
        indexes = {
                @Index(name = "idx_scheduler_settings_scheduler_name", columnList = "scheduler_name")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_scheduler_settings_scheduler_name", columnNames = {"scheduler_name"})
        }
)
public class SchedulerSetting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "scheduler_name", nullable = false, length = 100)
    private String schedulerName;

    @Column(nullable = false)
    private boolean enabled;

    @Column(nullable = false, length = 100)
    private String cron;

    @Column(nullable = false, length = 100)
    private String zone;
}
