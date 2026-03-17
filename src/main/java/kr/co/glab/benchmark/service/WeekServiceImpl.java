package kr.co.glab.benchmark.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.WeekFields;
import java.util.Locale;
import org.springframework.stereotype.Service;

@Service
public class WeekServiceImpl implements WeekService {

    @Override
    public String currentWeek() {
        return formatWeek(LocalDate.now());
    }

    @Override
    public String previousWeek() {
        return formatWeek(LocalDate.now().minusWeeks(1));
    }

    @Override
    public LocalDateTime weekStart(String week) {
        String[] parts = week.split("-W");
        int year = Integer.parseInt(parts[0]);
        int weekNumber = Integer.parseInt(parts[1]);
        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        LocalDate date = LocalDate.of(year, 1, 4)
                .with(weekFields.weekOfWeekBasedYear(), weekNumber)
                .with(weekFields.dayOfWeek(), 1);
        return date.atStartOfDay();
    }

    @Override
    public LocalDateTime weekEnd(String week) {
        return weekStart(week).plusDays(7).minusNanos(1);
    }

    private String formatWeek(LocalDate date) {
        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        int week = date.get(weekFields.weekOfWeekBasedYear());
        return "%d-W%02d".formatted(date.getYear(), week);
    }
}
