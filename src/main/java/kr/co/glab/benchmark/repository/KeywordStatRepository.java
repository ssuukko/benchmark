package kr.co.glab.benchmark.repository;

import java.util.List;
import java.util.Optional;
import kr.co.glab.benchmark.entity.KeywordStat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KeywordStatRepository extends JpaRepository<KeywordStat, Long> {

    List<KeywordStat> findTop10ByWeekAndModelNameOrderByCountDesc(String week, String modelName);

    List<KeywordStat> findByWeekOrderByCountDesc(String week);

    Optional<KeywordStat> findByWeekAndModelNameAndKeyword(String week, String modelName, String keyword);
}
