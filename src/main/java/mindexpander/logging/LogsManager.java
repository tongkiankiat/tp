package mindexpander.logging;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class LogsManager {
    private static final String LOGS_DIRECTORY = "logs";

    public static Path getLogFilePath(String filename) throws IOException {
        Path logsDir = Paths.get(LOGS_DIRECTORY);
        if (!Files.exists(logsDir)) {
            Files.createDirectories(logsDir);
        }
        return logsDir.resolve(filename);
    }
}
