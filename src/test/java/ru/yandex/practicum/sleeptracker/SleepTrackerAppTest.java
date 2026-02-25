package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SleepTrackerAppTest {

    private static SleepingSession session(LocalDateTime fall, LocalDateTime wake, SleepQuality q) {
        return new SleepingSession(fall, wake, q);
    }

    @Test
    void minDuration_emptyList_returnsZero() {
        MinDurationAnalyzer a = new MinDurationAnalyzer();
        SleepAnalysisResult r = a.apply(List.of());
        assertEquals(0L, r.getValue());
    }

    @Test
    void minDuration_severalSessions_returnsMinMinutes() {
        MinDurationAnalyzer a = new MinDurationAnalyzer();
        List<SleepingSession> sessions = List.of(
                session(LocalDateTime.of(2025, 10, 1, 22, 0), LocalDateTime.of(2025, 10, 2, 6, 0), SleepQuality.GOOD),
                session(LocalDateTime.of(2025, 10, 2, 14, 0), LocalDateTime.of(2025, 10, 2, 14, 50), SleepQuality.NORMAL)
        );
        SleepAnalysisResult r = a.apply(sessions);
        assertEquals(50L, r.getValue());
    }

    @Test
    void maxDuration_emptyList_returnsZero() {
        MaxDurationAnalyzer a = new MaxDurationAnalyzer();
        SleepAnalysisResult r = a.apply(List.of());
        assertEquals(0L, r.getValue());
    }

    @Test
    void maxDuration_severalSessions_returnsMaxMinutes() {
        MaxDurationAnalyzer a = new MaxDurationAnalyzer();
        List<SleepingSession> sessions = List.of(
                session(LocalDateTime.of(2025, 10, 1, 22, 0), LocalDateTime.of(2025, 10, 2, 6, 0), SleepQuality.GOOD),
                session(LocalDateTime.of(2025, 10, 2, 14, 0), LocalDateTime.of(2025, 10, 2, 14, 50), SleepQuality.NORMAL)
        );
        SleepAnalysisResult r = a.apply(sessions);
        assertEquals(480L, r.getValue());
    }

    @Test
    void averageDuration_emptyList_returnsZero() {
        AverageDurationAnalyzer a = new AverageDurationAnalyzer();
        SleepAnalysisResult r = a.apply(List.of());
        assertEquals(0L, r.getValue());
    }

    @Test
    void averageDuration_twoSessions_returnsRoundedAverage() {
        AverageDurationAnalyzer a = new AverageDurationAnalyzer();
        List<SleepingSession> sessions = List.of(
                session(LocalDateTime.of(2025, 10, 1, 0, 0), LocalDateTime.of(2025, 10, 1, 2, 0), SleepQuality.GOOD),
                session(LocalDateTime.of(2025, 10, 2, 0, 0), LocalDateTime.of(2025, 10, 2, 4, 0), SleepQuality.NORMAL)
        );
        SleepAnalysisResult r = a.apply(sessions);
        assertEquals(180L, r.getValue());
    }

    @Test
    void badQualityCount_noBad_returnsZero() {
        BadQualityCountAnalyzer a = new BadQualityCountAnalyzer();
        List<SleepingSession> sessions = List.of(
                session(LocalDateTime.of(2025, 10, 1, 22, 0), LocalDateTime.of(2025, 10, 2, 6, 0), SleepQuality.GOOD),
                session(LocalDateTime.of(2025, 10, 2, 23, 0), LocalDateTime.of(2025, 10, 3, 7, 0), SleepQuality.NORMAL)
        );
        SleepAnalysisResult r = a.apply(sessions);
        assertEquals(0L, r.getValue());
    }

    @Test
    void badQualityCount_twoBad_returnsTwo() {
        BadQualityCountAnalyzer a = new BadQualityCountAnalyzer();
        List<SleepingSession> sessions = List.of(
                session(LocalDateTime.of(2025, 10, 1, 22, 0), LocalDateTime.of(2025, 10, 2, 6, 0), SleepQuality.BAD),
                session(LocalDateTime.of(2025, 10, 2, 23, 0), LocalDateTime.of(2025, 10, 3, 7, 0), SleepQuality.NORMAL),
                session(LocalDateTime.of(2025, 10, 3, 23, 0), LocalDateTime.of(2025, 10, 4, 6, 0), SleepQuality.BAD)
        );
        SleepAnalysisResult r = a.apply(sessions);
        assertEquals(2L, r.getValue());
    }

    @Test
    void sleeplessNights_emptyList_returnsZero() {
        SleeplessNightsAnalyzer a = new SleeplessNightsAnalyzer();
        SleepAnalysisResult r = a.apply(List.of());
        assertEquals(0L, r.getValue());
    }

    @Test
    void sleeplessNights_oneNightWithSleep_noSleepless() {
        SleeplessNightsAnalyzer a = new SleeplessNightsAnalyzer();
        List<SleepingSession> sessions = List.of(
                session(LocalDateTime.of(2025, 10, 1, 23, 0), LocalDateTime.of(2025, 10, 2, 7, 0), SleepQuality.GOOD)
        );
        SleepAnalysisResult r = a.apply(sessions);
        assertEquals(0L, r.getValue());
    }

    @Test
    void sleeplessNights_dayOnlySleep_sleeplessNight() {
        SleeplessNightsAnalyzer a = new SleeplessNightsAnalyzer();
        List<SleepingSession> sessions = List.of(
                session(LocalDateTime.of(2025, 10, 2, 7, 0), LocalDateTime.of(2025, 10, 2, 11, 0), SleepQuality.NORMAL)
        );
        SleepAnalysisResult r = a.apply(sessions);
        assertEquals(1L, r.getValue());
    }

    @Test
    void sleeplessNights_skipOneNight_countsOneSleepless() {
        List<SleepingSession> sessions = List.of(
                session(LocalDateTime.of(2025, 10, 1, 23, 0), LocalDateTime.of(2025, 10, 2, 6, 0), SleepQuality.GOOD),
                session(LocalDateTime.of(2025, 10, 2, 23, 0), LocalDateTime.of(2025, 10, 3, 6, 0), SleepQuality.GOOD)
        );
        SleeplessNightsAnalyzer a = new SleeplessNightsAnalyzer();
        SleepAnalysisResult r = a.apply(sessions);
        assertEquals(1L, r.getValue());
    }

    @Test
    void chronotype_onlyOwlNights_returnsOwl() {
        ChronotypeAnalyzer a = new ChronotypeAnalyzer();
        List<SleepingSession> sessions = List.of(
                session(LocalDateTime.of(2025, 10, 1, 23, 30), LocalDateTime.of(2025, 10, 2, 9, 30), SleepQuality.GOOD),
                session(LocalDateTime.of(2025, 10, 2, 23, 15), LocalDateTime.of(2025, 10, 3, 10, 0), SleepQuality.GOOD)
        );
        SleepAnalysisResult r = a.apply(sessions);
        assertEquals("Сова", r.getValue());
    }

    @Test
    void chronotype_onlyLarkNights_returnsLark() {
        ChronotypeAnalyzer a = new ChronotypeAnalyzer();
        List<SleepingSession> sessions = List.of(
                session(LocalDateTime.of(2025, 10, 1, 21, 0), LocalDateTime.of(2025, 10, 2, 5, 0), SleepQuality.GOOD),
                session(LocalDateTime.of(2025, 10, 2, 20, 30), LocalDateTime.of(2025, 10, 3, 6, 0), SleepQuality.GOOD)
        );
        SleepAnalysisResult r = a.apply(sessions);
        assertEquals("Жаворонок", r.getValue());
    }

    @Test
    void chronotype_tie_returnsDove() {
        ChronotypeAnalyzer a = new ChronotypeAnalyzer();
        List<SleepingSession> sessions = List.of(
                session(LocalDateTime.of(2025, 10, 1, 23, 30), LocalDateTime.of(2025, 10, 2, 9, 30), SleepQuality.GOOD),
                session(LocalDateTime.of(2025, 10, 2, 21, 0), LocalDateTime.of(2025, 10, 3, 5, 0), SleepQuality.GOOD)
        );
        SleepAnalysisResult r = a.apply(sessions);
        assertEquals("Голубь", r.getValue());
    }

    @Test
    void chronotype_ignoresDaytimeSessions() {
        ChronotypeAnalyzer a = new ChronotypeAnalyzer();
        List<SleepingSession> sessions = List.of(
                session(LocalDateTime.of(2025, 10, 1, 14, 0), LocalDateTime.of(2025, 10, 1, 15, 0), SleepQuality.GOOD),
                session(LocalDateTime.of(2025, 10, 1, 21, 0), LocalDateTime.of(2025, 10, 2, 5, 0), SleepQuality.GOOD)
        );
        SleepAnalysisResult r = a.apply(sessions);
        assertEquals("Жаворонок", r.getValue());
    }
}
