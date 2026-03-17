package kr.co.glab.benchmark.repository;

import java.util.Optional;
import kr.co.glab.benchmark.entity.SchedulerSetting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SchedulerSettingRepository extends JpaRepository<SchedulerSetting, Long> {

    Optional<SchedulerSetting> findBySchedulerName(String schedulerName);
}
