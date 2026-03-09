package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SleeplessNightsAnalyzerTest {

    private static SleepingSession session(LocalDateTime fall, LocalDateTime wake, SleepQuality quality) {
        return new SleepingSession(fall, wake, quality);
    }

    @Test
    void emptyList_returnsZero() {
        SleeplessNightsAnalyzer analyzer = new SleeplessNightsAnalyzer();
        SleepAnalysisResult result = analyzer.apply(List.of());
        assertEquals(0L, result.getValue());
    }

    @Test
    void oneNightWithSleep_noSleepless() {
        SleeplessNightsAnalyzer analyzer = new SleeplessNightsAnalyzer();
        List<SleepingSession> sessions = List.of(
                session(LocalDateTime.of(2025, 10, 1, 23, 0),
                        LocalDateTime.of(2025, 10, 2, 7, 0),
                        SleepQuality.GOOD)
        );
        SleepAnalysisResult result = analyzer.apply(sessions);
        assertEquals(0L, result.getValue());
    }

    @Test
    void dayOnlySleep_sleeplessNight() {
        SleeplessNightsAnalyzer analyzer = new SleeplessNightsAnalyzer();
        List<SleepingSession> sessions = List.of(
                session(LocalDateTime.of(2025, 10, 2, 7, 0),
                        LocalDateTime.of(2025, 10, 2, 11, 0),
                        SleepQuality.NORMAL)
        );
        SleepAnalysisResult result = analyzer.apply(sessions);
        assertEquals(1L, result.getValue());
    }

    @Test
    void skipOneNight_countsOneSleepless() {
        SleeplessNightsAnalyzer analyzer = new SleeplessNightsAnalyzer();
        List<SleepingSession> sessions = List.of(
                session(LocalDateTime.of(2025, 10, 1, 23, 0),
                        LocalDateTime.of(2025, 10, 2, 6, 0),
                        SleepQuality.GOOD),
                session(LocalDateTime.of(2025, 10, 2, 23, 0),
                        LocalDateTime.of(2025, 10, 3, 6, 0),
                        SleepQuality.GOOD)
        );
        SleepAnalysisResult result = analyzer.apply(sessions);
        assertEquals(1L, result.getValue());
    }

    @Test
    void firstSessionStartsAfterMidnight_countsNightsCorrectly() {
        SleeplessNightsAnalyzer analyzer = new SleeplessNightsAnalyzer();
        List<SleepingSession> sessions = List.of(
                session(LocalDateTime.of(2025, 10, 1, 1, 0),
                        LocalDateTime.of(2025, 10, 1, 8, 0),
                        SleepQuality.GOOD)
        );
        SleepAnalysisResult result = analyzer.apply(sessions);
        assertEquals(0L, result.getValue());
    }

    @Test
    void intervalSpansDifferentMonths_countsSleeplessAcrossBoundary() {
        SleeplessNightsAnalyzer analyzer = new SleeplessNightsAnalyzer();
        List<SleepingSession> sessions = List.of(
                session(LocalDateTime.of(2025, 1, 31, 23, 0),
                        LocalDateTime.of(2025, 2, 1, 6, 0),
                        SleepQuality.GOOD),
                session(LocalDateTime.of(2025, 2, 1, 23, 0),
                        LocalDateTime.of(2025, 2, 2, 6, 0),
                        SleepQuality.GOOD)
        );
        SleepAnalysisResult result = analyzer.apply(sessions);
        assertEquals(1L, result.getValue());
    }
}

