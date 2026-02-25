package ru.yandex.practicum.sleeptracker;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.function.Function;
import java.util.stream.LongStream;

public class SleeplessNightsAnalyzer implements Function<List<SleepingSession>, SleepAnalysisResult> {
    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        if (sessions.isEmpty()) {
            return new SleepAnalysisResult("Количество бессонных ночей", 0L);
        }
        LocalDateTime loggingStart = sessions.get(0).getFallAsleep();
        LocalDateTime loggingEnd = sessions.get(sessions.size() - 1).getWakeUp();
        LocalDate startDate = loggingStart.toLocalDate();
        LocalDate endDate = loggingEnd.toLocalDate();
        long days = ChronoUnit.DAYS.between(startDate, endDate) + 1;
        long sleepless = LongStream.range(0, days)
                .mapToObj(startDate::plusDays)
                .filter(date -> nightWithinLogging(date, loggingStart, loggingEnd))
                .filter(date -> noSessionIntersectsNight(sessions, date))
                .count();
        return new SleepAnalysisResult("Количество бессонных ночей", sleepless);
    }

    private static boolean nightWithinLogging(LocalDate date,
                                              LocalDateTime loggingStart,
                                              LocalDateTime loggingEnd) {
        LocalDateTime nightStart = date.atStartOfDay();
        LocalDateTime nightEnd = date.atTime(6, 0);
        return nightEnd.isAfter(loggingStart) && nightStart.isBefore(loggingEnd);
    }

    private static boolean noSessionIntersectsNight(List<SleepingSession> sessions, LocalDate date) {
        LocalDateTime nightStart = date.atStartOfDay();
        LocalDateTime nightEnd = date.atTime(6, 0);
        return sessions.stream()
                .noneMatch(s -> s.getFallAsleep().isBefore(nightEnd) && s.getWakeUp().isAfter(nightStart));
    }
}
