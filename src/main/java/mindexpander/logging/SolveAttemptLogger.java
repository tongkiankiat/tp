package mindexpander.logging;

import mindexpander.data.question.Question;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Static logger implementation for tracking question solving attempts.
 * <p>
 * Records each attempt in a pipe-delimited log file with the following format:
 * <pre>
 * Timestamp|Question|Result
 * 2025-04-04 14:30:00|MCQ: What is...|CORRECT
 * </pre>
 * Inherits common functionality from BaseLogger while maintaining static access.
 * </p>
 *
 * @author lwenyi1
 * @version 2.0
 * @since 2025-04-04
 */
public class SolveAttemptLogger extends BaseLogger {
    /** Log filename within the logs directory */
    private static final String LOG_FILENAME = "solveAttemptLogs";

    /** Field delimiter for log entries */
    private static final String DELIMITER = "|";

    /** Timestamp format for log entries */
    private static final DateTimeFormatter TIMESTAMP_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /** Path to the log file */
    private static final Path LOG_FILE;

    // Static initialization block
    static {
        try {
            LOG_FILE = LogsManager.getLogFilePath(LOG_FILENAME);
            if (!Files.exists(LOG_FILE) || Files.size(LOG_FILE) == 0) {
                String header = String.join(DELIMITER,
                        "Timestamp", "Question", "Result") + "\n";
                Files.write(LOG_FILE, header.getBytes());
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize SolveAttemptLogger", e);
        }
    }

    /**
     * Records a question attempt in the log file.
     *
     * @param question The question that was attempted
     * @param isCorrect Whether the attempt was correct
     * @throws RuntimeException if logger initialization failed
     */
    public static void logAttempt(Question question, boolean isCorrect) {
        String record = String.join(DELIMITER,
                LocalDateTime.now().format(TIMESTAMP_FORMAT),
                escapeSpecialCharacters(question.toStringNoAnswer()),
                isCorrect ? "CORRECT" : "WRONG");

        try {
            Files.write(LOG_FILE, (record + "\n").getBytes(),
                    StandardOpenOption.APPEND, StandardOpenOption.CREATE);
        } catch (IOException e) {
            printLogMessage("Error writing to log: " + e.getMessage());
        }
    }
}
