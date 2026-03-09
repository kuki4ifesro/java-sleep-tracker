package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ChronotypeAnalyzerTest {

    private static SleepingSession session(LocalDateTime fall, LocalDateTime wake, SleepQuality quality) {
        return new SleepingSession(fall, wake, quality);
    }

    @Test
    void onlyOwlNights_returnsOwl() {
        ChronotypeAnalyzer analyzer = new ChronotypeAnalyzer();
        List<SleepingSession> sessions = List.of(
                session(LocalDateTime.of(2025, 10, 1, 23, 30),
                        LocalDateTime.of(2025, 10, 2, 9, 30),
                        SleepQuality.GOOD),
                session(LocalDateTime.of(2025, 10, 2, 23, 15),
                        LocalDateTime.of(2025, 10, 3, 10, 0),
                        SleepQuality.GOOD)
        );
        SleepAnalysisResult result = analyzer.apply(sessions);
        assertEquals("Сова", result.getValue());
    }

    @Test
    void onlyLarkNights_returnsLark() {
        ChronotypeAnalyzer analyzer = new ChronotypeAnalyzer();
        List<SleepingSession> sessions = List.of(
                session(LocalDateTime.of(2025, 10, 1, 21, 0),
                        LocalDateTime.of(2025, 10, 2, 5, 0),
                        SleepQuality.GOOD),
                session(LocalDateTime.of(2025, 10, 2, 20, 30),
                        LocalDateTime.of(2025, 10, 3, 6, 0),
                        SleepQuality.GOOD)
        );
        SleepAnalysisResult result = analyzer.apply(sessions);
        assertEquals("Жаворонок", result.getValue());
    }

    @Test
    void tie_returnsDove() {
        ChronotypeAnalyzer analyzer = new ChronotypeAnalyzer();
        List<SleepingSession> sessions = List.of(
                session(LocalDateTime.of(2025, 10, 1, 23, 30),
                        LocalDateTime.of(2025, 10, 2, 9, 30),
                        SleepQuality.GOOD),
                session(LocalDateTime.of(2025, 10, 2, 21, 0),
                        LocalDateTime.of(2025, 10, 3, 5, 0),
                        SleepQuality.GOOD)
        );
        SleepAnalysisResult result = analyzer.apply(sessions);
        assertEquals("Голубь", result.getValue());
    }

    @Test
    void ignoresDaytimeSessions() {
        ChronotypeAnalyzer analyzer = new ChronotypeAnalyzer();
        List<SleepingSession> sessions = List.of(
                session(LocalDateTime.of(2025, 10, 1, 14, 0),
                        LocalDateTime.of(2025, 10, 1, 15, 0),
                        SleepQuality.GOOD),
                session(LocalDateTime.of(2025, 10, 1, 21, 0),
                        LocalDateTime.of(2025, 10, 2, 5, 0),
                        SleepQuality.GOOD)
        );
        SleepAnalysisResult result = analyzer.apply(sessions);
        assertEquals("Жаворонок", result.getValue());
    }

    @Test
    void nightCrossingMidnightBefore23StillCountsAsOwl() {
        ChronotypeAnalyzer analyzer = new ChronotypeAnalyzer();
        List<SleepingSession> sessions = List.of(
                session(LocalDateTime.of(2025, 10, 1, 22, 30),
                        LocalDateTime.of(2025, 10, 2, 10, 0),
                        SleepQuality.GOOD)
        );
        SleepAnalysisResult result = analyzer.apply(sessions);
        assertEquals("Сова", result.getValue());
    }
}

