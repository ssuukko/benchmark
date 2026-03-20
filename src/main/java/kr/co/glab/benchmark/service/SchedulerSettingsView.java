package kr.co.glab.benchmark.service;

public record SchedulerSettingsView(
        String schedulerName,
        boolean enabled,
        String cron,
        String zone
) {
    public boolean fixedDailyTime() {
        String[] parts = cronParts();
        if (parts.length != 6) {
            return false;
        }

        return isNumber(parts[0]) && isNumber(parts[1]) && isNumber(parts[2])
                && "*".equals(parts[3]) && "*".equals(parts[4]) && "*".equals(parts[5]);
    }

    public boolean isFixedDailyTime() {
        return fixedDailyTime();
    }

    public int hour() {
        return cronPart(2);
    }

    public int minute() {
        return cronPart(1);
    }

    public int second() {
        return cronPart(0);
    }

    public String meridiem() {
        return hour() < 12 ? "AM" : "PM";
    }

    public String getMeridiem() {
        return meridiem();
    }

    public String meridiemLabel() {
        return "AM".equals(meridiem()) ? "오전" : "오후";
    }

    public String getMeridiemLabel() {
        return meridiemLabel();
    }

    public int displayHour() {
        int hour = hour();
        int normalized = hour % 12;
        return normalized == 0 ? 12 : normalized;
    }

    public int getDisplayHour() {
        return displayHour();
    }

    public String displayTime() {
        return String.format("%02d:%02d:%02d", hour(), minute(), second());
    }

    public String getDisplayTime() {
        return displayTime();
    }

    public String scheduleSummary() {
        if (fixedDailyTime()) {
            return "%s %d:%02d:%02d".formatted(meridiemLabel(), displayHour(), minute(), second());
        }

        return "사용자 정의 일정";
    }

    public String getScheduleSummary() {
        return scheduleSummary();
    }

    public String customScheduleWarning() {
        return "현재 저장값은 단순 시간 형식이 아닌 사용자 정의 일정입니다. 저장하면 "
                + displayTime()
                + " 기준의 매일 반복 일정으로 변경됩니다.";
    }

    public String getCustomScheduleWarning() {
        return customScheduleWarning();
    }

    private int cronPart(int index) {
        String[] parts = cronParts();
        if (parts.length != 6) {
            return 0;
        }

        try {
            return Integer.parseInt(parts[index]);
        } catch (NumberFormatException exception) {
            return 0;
        }
    }

    private String[] cronParts() {
        return String.valueOf(cron).trim().split("\\s+");
    }

    private boolean isNumber(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException exception) {
            return false;
        }
    }
}
