package ru.yandex.practicum.sleeptracker;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.function.Function;
import java.util.stream.LongStream;

public class SleeplessNightsAnalyzer implements Function<List<SleepingSession>, SleepAnalysisResult> {
    private static final LocalTime NIGHT_END = LocalTime.of(6, 0);

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        if (sessions.isEmpty()) {
            return new SleepAnalysisResult("Количество бессонных ночей", 0L);
        }
        LocalDate startDate = sessions.get(0).getFallAsleep().toLocalDate();
        LocalDate endDate = sessions.get(sessions.size() - 1).getWakeUp().toLocalDate();
        boolean skipFirstNight = sessions.size() == 1
                && !sessions.get(0).getFallAsleep().toLocalTime().isBefore(NIGHT_END)
                && !noSessionIntersectsNight(sessions, startDate.plusDays(1));
        LocalDate firstNight = skipFirstNight ? startDate.plusDays(1) : startDate;
        if (firstNight.isAfter(endDate)) {
            return new SleepAnalysisResult("Количество бессонных ночей", 0L);
        }
        long days = ChronoUnit.DAYS.between(firstNight, endDate) + 1;
        long sleepless = LongStream.range(0, days)
                .mapToObj(firstNight::plusDays)
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
