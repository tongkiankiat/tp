package mindexpander.logging;

import mindexpander.data.question.Question;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SolveAttemptLogger {
    private static final String LOG_FILENAME = "solveAttemptLogs";
    private static final String DELIMITER = "|";
    private static final DateTimeFormatter TIMESTAMP_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // Static initializer for the log file path
    private static final Path LOG_FILE;

    static {
        try {
            LOG_FILE = LogsManager.getLogFilePath(LOG_FILENAME);
            // Initialize log file with header if it doesn't exist or is empty
            if (!Files.exists(LOG_FILE) || Files.size(LOG_FILE) == 0) {
                String header = String.join(DELIMITER,
                        "Timestamp", "Question", "Result") + "\n";
                Files.write(LOG_FILE, header.getBytes());
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize SolveAttemptLogger", e);
        }
    }

    public static void logAttempt(Question question, boolean isCorrect) {
        String record = String.join(DELIMITER,
                LocalDateTime.now().format(TIMESTAMP_FORMAT),
                escape(question.toStringNoAnswer()),
                isCorrect ? "CORRECT" : "WRONG");

        try {
            Files.write(LOG_FILE, (record + "\n").getBytes(),
                    StandardOpenOption.APPEND, StandardOpenOption.CREATE);
        } catch (IOException e) {
            System.err.println("[SolveAttemptLogger] Error writing to log: " + e.getMessage());
        }
    }

    private static String escape(String input) {
        if (input == null) return "";
        return input.replace(DELIMITER, "\\" + DELIMITER)
                .replace("\n", "\\n")
                .replace("\r", "\\r");
    }
}
