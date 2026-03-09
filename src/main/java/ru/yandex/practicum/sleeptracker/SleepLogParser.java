package ru.yandex.practicum.sleeptracker;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class SleepLogParser {
    private static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");

    private SleepLogParser() {
    }

    public static List<SleepingSession> parse(Path path) throws IOException {
        try (Stream<String> lines = Files.lines(path)) {
            return lines
                    .filter(line -> !line.isBlank())
                    .map(SleepLogParser::parseLine)
                    .collect(Collectors.toList());
        }
    }

    private static SleepingSession parseLine(String line) {
        String[] parts = line.split(";");
        LocalDateTime fallAsleep = LocalDateTime.parse(parts[0].trim(), FORMAT);
        LocalDateTime wakeUp = LocalDateTime.parse(parts[1].trim(), FORMAT);
        SleepQuality quality = SleepQuality.valueOf(parts[2].trim());
        return new SleepingSession(fallAsleep, wakeUp, quality);
    }
}
