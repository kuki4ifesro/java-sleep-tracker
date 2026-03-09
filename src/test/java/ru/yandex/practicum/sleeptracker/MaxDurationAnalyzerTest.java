package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MaxDurationAnalyzerTest {

    private static SleepingSession session(LocalDateTime fall, LocalDateTime wake, SleepQuality quality) {
        return new SleepingSession(fall, wake, quality);
    }

    @Test
    void emptyList_returnsZero() {
        MaxDurationAnalyzer analyzer = new MaxDurationAnalyzer();
        SleepAnalysisResult result = analyzer.apply(List.of());
        assertEquals(0L, result.getValue());
    }

    @Test
    void severalSessions_returnsMaxMinutes() {
        MaxDurationAnalyzer analyzer = new MaxDurationAnalyzer();
        List<SleepingSession> sessions = List.of(
                session(LocalDateTime.of(2025, 10, 1, 22, 0),
                        LocalDateTime.of(2025, 10, 2, 6, 0),
                        SleepQuality.GOOD),
                session(LocalDateTime.of(2025, 10, 2, 14, 0),
                        LocalDateTime.of(2025, 10, 2, 14, 50),
                        SleepQuality.NORMAL)
        );
        SleepAnalysisResult result = analyzer.apply(sessions);
        assertEquals(480L, result.getValue());
    }
}

