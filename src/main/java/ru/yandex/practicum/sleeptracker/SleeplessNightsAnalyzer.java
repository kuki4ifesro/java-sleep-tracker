package ru.yandex.practicum.sleeptracker;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Function;
import java.util.stream.LongStream;

public class SleeplessNightsAnalyzer implements Function<List<SleepingSession>, SleepAnalysisResult> {
    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        if (sessions.isEmpty()) {
            return new SleepAnalysisResult("Количество бессонных ночей", 0L);
        }
        LocalDate start = sessions.get(0).getFallAsleep().toLocalDate();
        LocalDate end = sessions.get(sessions.size() - 1).getWakeUp().toLocalDate();
        long totalDays = java.time.temporal.ChronoUnit.DAYS.between(start, end) + 1;
        long sleepless = LongStream.range(0, totalDays)
                .mapToObj(start::plusDays)
                .filter(date -> noSessionIntersectsNight(sessions, date))
                .count();
        return new SleepAnalysisResult("Количество бессонных ночей", sleepless);
    }

    private static boolean noSessionIntersectsNight(List<SleepingSession> sessions, LocalDate date) {
        LocalDateTime nightStart = date.atStartOfDay();
        LocalDateTime nightEnd = date.atTime(6, 0);
        return sessions.stream()
                .noneMatch(s -> s.getFallAsleep().isBefore(nightEnd) && s.getWakeUp().isAfter(nightStart));
    }
}
