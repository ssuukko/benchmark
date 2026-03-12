package kr.co.glab.benchmark.repository;

import kr.co.glab.benchmark.entity.TrendStat;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrendStatRepository extends JpaRepository<TrendStat, Long> {

    List<TrendStat> findByWeekOrderByMentionCountDesc(String week);
}
