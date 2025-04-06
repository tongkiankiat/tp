package mindexpander.logging;

import mindexpander.common.Messages;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ErrorLogger extends BaseLogger {
    // Attributes
    private static final String LOG_FILENAME = "errorLogs";
    private static final DateTimeFormatter TIMESTAMP_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final Path LOG_FILEPATH;

    // Constructor
    // Static initialization block
    static {
        try {
            LOG_FILEPATH = LogsManager.getLogFilePath(LOG_FILENAME);
            if (!Files.exists(LOG_FILEPATH) || Files.size(LOG_FILEPATH) == 0) {
                String header = String.join(Messages.ERROR_LOGGER_DELIMITER,
                        "Timestamp", "Input", "Error Message") + "\n";
                Files.write(LOG_FILEPATH, header.getBytes());
            }
        } catch (IOException e) {
            throw new RuntimeException(Messages.UNABLE_TO_INIITIALISE_ERROR_LOGGER, e);
        }
    }

    public static void logError(String input, String errorMessage) {
        String record = String.join(Messages.ERROR_LOGGER_DELIMITER,
                LocalDateTime.now().format(TIMESTAMP_FORMAT),
                input,
                errorMessage);

        try {
            Files.write(LOG_FILEPATH, (record + "\n").getBytes(),
                    StandardOpenOption.APPEND, StandardOpenOption.CREATE);
        } catch (IOException e) {
            printLogMessage("Error writing to log: " + e.getMessage());
        }
    }
}
