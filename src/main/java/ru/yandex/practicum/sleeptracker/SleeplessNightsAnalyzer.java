package ru.yandex.practicum.sleeptracker;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.function.Function;

public class SleeplessNightsAnalyzer implements Function<List<SleepingSession>, SleepAnalysisResult> {

    private static final LocalTime NIGHT_END = LocalTime.of(6, 0);
    private static final String DESCRIPTION = "Количество бессонных ночей";

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        if (sessions.isEmpty()) {
            return new SleepAnalysisResult(DESCRIPTION, 0L);
        }

        LocalDate firstNight = sessions.get(0).getFallAsleep().toLocalDate();
        LocalDate lastNight = sessions.get(sessions.size() - 1).getWakeUp().toLocalDate();

        if (firstNight.isAfter(lastNight)) {
            return new SleepAnalysisResult(DESCRIPTION, 0L);
        }

        long totalNights = ChronoUnit.DAYS.between(firstNight, lastNight) + 1;
        long nightSessions = sessions.stream()
                .filter(SleeplessNightsAnalyzer::isNightSleep)
                .count();

        long sleepless = totalNights - nightSessions;

        if (sessions.size() == 1 && isNightSleep(sessions.get(0))) {
            sleepless = Math.max(0, sleepless - 1);
        }

        return new SleepAnalysisResult(DESCRIPTION, sleepless);
    }

    static boolean isNightSleep(SleepingSession session) {
        LocalDate date = session.getWakeUp().toLocalDate();
        LocalDateTime nightStart = date.atStartOfDay();
        LocalDateTime nightEnd = date.atTime(NIGHT_END);
        return session.getFallAsleep().isBefore(nightEnd) && session.getWakeUp().isAfter(nightStart);
    }
}
