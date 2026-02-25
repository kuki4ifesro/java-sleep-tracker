package ru.yandex.practicum.sleeptracker;

import java.time.LocalTime;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ChronotypeAnalyzer implements Function<List<SleepingSession>, SleepAnalysisResult> {
    private static final LocalTime OWL_SLEEP_AFTER = LocalTime.of(23, 0);
    private static final LocalTime OWL_WAKE_AFTER = LocalTime.of(9, 0);
    private static final LocalTime LARK_SLEEP_BEFORE = LocalTime.of(22, 0);
    private static final LocalTime LARK_WAKE_BEFORE = LocalTime.of(7, 0);

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        List<Chronotype> nightTypes = nightSessions(sessions)
                .map(this::classifyNight)
                .collect(Collectors.toList());
        long owl = nightTypes.stream().filter(t -> t == Chronotype.OWL).count();
        long lark = nightTypes.stream().filter(t -> t == Chronotype.LARK).count();
        long dove = nightTypes.stream().filter(t -> t == Chronotype.DOVE).count();
        String result;
        if (owl > lark && owl > dove) {
            result = "Сова";
        } else if (lark > owl && lark > dove) {
            result = "Жаворонок";
        } else {
            result = "Голубь";
        }
        return new SleepAnalysisResult("Хронотип пользователя", result);
    }

    private static Stream<SleepingSession> nightSessions(List<SleepingSession> sessions) {
        return sessions.stream()
                .filter(s -> s.getFallAsleep().isBefore(s.getWakeUp().toLocalDate().atTime(6, 0))
                        && s.getWakeUp().isAfter(s.getWakeUp().toLocalDate().atStartOfDay()));
    }

    private Chronotype classifyNight(SleepingSession s) {
        LocalTime fall = s.getFallAsleep().toLocalTime();
        LocalTime wake = s.getWakeUp().toLocalTime();
        if (fall.isAfter(OWL_SLEEP_AFTER) && wake.isAfter(OWL_WAKE_AFTER)) {
            return Chronotype.OWL;
        }
        if (!fall.isAfter(LARK_SLEEP_BEFORE) && !wake.isAfter(LARK_WAKE_BEFORE)) {
            return Chronotype.LARK;
        }
        return Chronotype.DOVE;
    }

    private enum Chronotype { OWL, LARK, DOVE }
}
