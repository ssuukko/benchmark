package kr.co.glab.benchmark.repository;

import kr.co.glab.benchmark.entity.TrendStat;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrendStatRepository extends JpaRepository<TrendStat, Long> {

    List<TrendStat> findByWeekOrderByMentionCountDesc(String week);

    Optional<TrendStat> findByWeekAndModelName(String week, String modelName);

    void deleteByWeek(String week);
}
