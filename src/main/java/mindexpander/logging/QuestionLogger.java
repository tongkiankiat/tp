package mindexpander.logging;

import mindexpander.data.question.Question;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Logs all question-related actions: add, delete, and edit.
 * Logs include: timestamp, action type, question type, and full question (with answers/options).
 */
public class QuestionLogger extends BaseLogger {
    private static final String LOG_FILENAME = "questionLogs";
    private static final String DELIMITER = "|";
    private static final DateTimeFormatter TIMESTAMP_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static Path logFile;

    private static void initializeLogger() {
        if (logFile != null) {
            return;
        }
        try {
            logFile = LogsManager.getLogFilePath(LOG_FILENAME);
            if (!Files.exists(logFile) || Files.size(logFile) == 0) {
                String header = String.join(DELIMITER,
                        "Timestamp", "Action", "QuestionType", "FullQuestion") + "\n";
                Files.write(logFile, header.getBytes(), StandardOpenOption.CREATE);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize QuestionLogger", e);
        }
    }

    private static void log(String action, Question question) {
        initializeLogger();
        String record = String.join(DELIMITER,
                LocalDateTime.now().format(TIMESTAMP_FORMAT),
                action.toUpperCase(),
                question.getType().toString(),
                escapeSpecialCharacters(question.toString()));

        try {
            Files.write(logFile, (record + "\n").getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            printLogMessage("Error writing to question log: " + e.getMessage());
        }
    }

    //Public access points for other commands to use
    public static void logAddedQuestion(Question question) {
        log("ADDED", question);
    }

    public static void logDeletedQuestion(Question question) {
        log("DELETED", question);
    }

    public static void logEditedQuestion(Question question) {
        log("EDITED", question);
    }
}
