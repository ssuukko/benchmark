package kr.co.glab.benchmark.service;

import java.time.ZoneId;
import jakarta.transaction.Transactional;
import kr.co.glab.benchmark.config.SchedulerProperties;
import kr.co.glab.benchmark.entity.SchedulerSetting;
import kr.co.glab.benchmark.repository.SchedulerSettingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.support.CronExpression;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SchedulerSettingsServiceImpl implements SchedulerSettingsService {

    public static final String HACKER_NEWS_SCHEDULER = "HACKER_NEWS";

    private final SchedulerProperties schedulerProperties;
    private final SchedulerSettingRepository schedulerSettingRepository;

    @Override
    @Transactional
    public SchedulerSettingsView getHackerNewsSettings() {
        return toView(loadOrCreateHackerNewsSetting());
    }

    @Override
    @Transactional
    public SchedulerSettingsView updateHackerNewsSettings(boolean enabled, String cron, String zone) {
        CronExpression.parse(cron);
        ZoneId.of(zone);

        SchedulerSetting setting = loadOrCreateHackerNewsSetting();
        setting.setEnabled(enabled);
        setting.setCron(cron);
        setting.setZone(zone);

        return toView(schedulerSettingRepository.save(setting));
    }

    private SchedulerSetting loadOrCreateHackerNewsSetting() {
        return schedulerSettingRepository.findBySchedulerName(HACKER_NEWS_SCHEDULER)
                .orElseGet(this::createDefaultHackerNewsSetting);
    }

    private SchedulerSetting createDefaultHackerNewsSetting() {
        SchedulerSetting setting = new SchedulerSetting();
        setting.setSchedulerName(HACKER_NEWS_SCHEDULER);
        setting.setEnabled(schedulerProperties.enabled());
        setting.setCron(schedulerProperties.cron());
        setting.setZone(schedulerProperties.zone());
        return schedulerSettingRepository.save(setting);
    }

    private SchedulerSettingsView toView(SchedulerSetting setting) {
        return new SchedulerSettingsView(
                setting.getSchedulerName(),
                setting.isEnabled(),
                setting.getCron(),
                setting.getZone()
        );
    }
}
