package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AverageDurationAnalyzerTest {

    private static SleepingSession session(LocalDateTime fall, LocalDateTime wake, SleepQuality quality) {
        return new SleepingSession(fall, wake, quality);
    }

    @Test
    void emptyList_returnsZero() {
        AverageDurationAnalyzer analyzer = new AverageDurationAnalyzer();
        SleepAnalysisResult result = analyzer.apply(List.of());
        assertEquals(0L, result.getValue());
    }

    @Test
    void twoSessions_returnsRoundedAverage() {
        AverageDurationAnalyzer analyzer = new AverageDurationAnalyzer();
        List<SleepingSession> sessions = List.of(
                session(LocalDateTime.of(2025, 10, 1, 0, 0),
                        LocalDateTime.of(2025, 10, 1, 2, 0),
                        SleepQuality.GOOD),
                session(LocalDateTime.of(2025, 10, 2, 0, 0),
                        LocalDateTime.of(2025, 10, 2, 4, 0),
                        SleepQuality.NORMAL)
        );
        SleepAnalysisResult result = analyzer.apply(sessions);
        assertEquals(180L, result.getValue());
    }
}

