package kr.co.glab.benchmark.repository;

import java.util.Optional;
import kr.co.glab.benchmark.entity.WeeklyReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WeeklyReportRepository extends JpaRepository<WeeklyReport, Long> {

    Optional<WeeklyReport> findByWeek(String week);
}
