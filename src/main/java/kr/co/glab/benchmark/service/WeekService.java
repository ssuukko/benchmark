package kr.co.glab.benchmark.service;

import java.time.LocalDateTime;

public interface WeekService {

    String currentWeek();

    String previousWeek();

    LocalDateTime weekStart(String week);

    LocalDateTime weekEnd(String week);
}
