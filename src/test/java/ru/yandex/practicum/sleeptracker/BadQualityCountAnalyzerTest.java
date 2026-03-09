package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BadQualityCountAnalyzerTest {

    private static SleepingSession session(LocalDateTime fall, LocalDateTime wake, SleepQuality quality) {
        return new SleepingSession(fall, wake, quality);
    }

    @Test
    void noBad_returnsZero() {
        BadQualityCountAnalyzer analyzer = new BadQualityCountAnalyzer();
        List<SleepingSession> sessions = List.of(
                session(LocalDateTime.of(2025, 10, 1, 22, 0),
                        LocalDateTime.of(2025, 10, 2, 6, 0),
                        SleepQuality.GOOD),
                session(LocalDateTime.of(2025, 10, 2, 23, 0),
                        LocalDateTime.of(2025, 10, 3, 7, 0),
                        SleepQuality.NORMAL)
        );
        SleepAnalysisResult result = analyzer.apply(sessions);
        assertEquals(0L, result.getValue());
    }

    @Test
    void twoBad_returnsTwo() {
        BadQualityCountAnalyzer analyzer = new BadQualityCountAnalyzer();
        List<SleepingSession> sessions = List.of(
                session(LocalDateTime.of(2025, 10, 1, 22, 0),
                        LocalDateTime.of(2025, 10, 2, 6, 0),
                        SleepQuality.BAD),
                session(LocalDateTime.of(2025, 10, 2, 23, 0),
                        LocalDateTime.of(2025, 10, 3, 7, 0),
                        SleepQuality.NORMAL),
                session(LocalDateTime.of(2025, 10, 3, 23, 0),
                        LocalDateTime.of(2025, 10, 4, 6, 0),
                        SleepQuality.BAD)
        );
        SleepAnalysisResult result = analyzer.apply(sessions);
        assertEquals(2L, result.getValue());
    }
}

