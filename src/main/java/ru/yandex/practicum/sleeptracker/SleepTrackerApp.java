package ru.yandex.practicum.sleeptracker;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SleepTrackerApp {

    private final List<Function<List<SleepingSession>, SleepAnalysisResult>> analyzers = Stream.of(
            new MinDurationAnalyzer(),
            new MaxDurationAnalyzer(),
            new AverageDurationAnalyzer(),
            new BadQualityCountAnalyzer(),
            new SleeplessNightsAnalyzer(),
            new ChronotypeAnalyzer()
    ).collect(Collectors.toList());

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Укажите путь к файлу лога сна.");
            return;
        }
        Path path = Paths.get(args[0]);
        List<SleepingSession> sessions;
        try {
            sessions = SleepLogParser.parse(path);
        } catch (IOException e) {
            System.out.println("Ошибка чтения файла: " + e.getMessage());
            return;
        }
        SleepTrackerApp app = new SleepTrackerApp();
        app.analyzers.stream()
                .map(f -> f.apply(sessions))
                .forEach(r -> System.out.println(r.getDescription() + ": " + r.getValue()));
    }
}
