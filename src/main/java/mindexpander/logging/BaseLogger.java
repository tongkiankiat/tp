package mindexpander.logging;

import mindexpander.ui.TextUi;

/**
 * Base logger class that provides common logging functionality
 * and integrates with the TextUi for displaying log messages.
 */
public abstract class BaseLogger {
    protected final TextUi textUi;
    private final String loggerName;

    protected BaseLogger(TextUi textUi, String loggerName) {
        this.textUi = textUi;
        this.loggerName = loggerName;
    }

    /**
     * Prints a log message to both the UI and system output
     * @param message The message to log
     * @param level The severity level (INFO, WARNING, ERROR)
     */
    protected void printLogMessage(String message, LogLevel level) {
        String formattedMessage = String.format("[%s %s] %s",
                loggerName, level, message);

        // Print to UI
        textUi.printToUser(formattedMessage);

        // Also print to system out for backup
        System.out.println(formattedMessage);
    }

    protected enum LogLevel {
        INFO, WARNING, ERROR
    }

    protected String escapeSpecialCharacters(String input) {
        if (input == null) return "";
        return input.replace("|", "\\|")
                .replace("\n", "\\n")
                .replace("\r", "\\r");
    }
}
