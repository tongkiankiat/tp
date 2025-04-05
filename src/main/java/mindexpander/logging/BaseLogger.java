package mindexpander.logging;

import mindexpander.ui.TextUi;

/**
 * Provides foundational logging functionality for all logger implementations.
 * <p>
 * This abstract class serves as the base for all loggers in the system, offering:
 * <ul>
 *   <li>Common message formatting</li>
 *   <li>TextUi integration for user-facing messages</li>
 *   <li>Standardized log levels</li>
 *   <li>Special character escaping</li>
 * </ul>
 * Supports both instance-based and static logging patterns.
 * </p>
 *
 * @author lwenyi1
 * @version 1.0
 * @since 2025-04-04
 */
public abstract class BaseLogger {
    /**
     * Prints a formatted log message to both UI and system output.
     *
     * @param message The message content to log
     */
    protected static void printLogMessage(String message) {
        TextUi.printErrorToUser(message);
    }

    /**
     * Escapes special characters in log messages to maintain format integrity.
     *
     * @param input The raw string to be escaped
     * @return The escaped string, or empty string if input is null
     */
    protected static String escapeSpecialCharacters(String input) {
        if (input == null) {
            return "";
        }
        return input.replace("|", "\\|")
                .replace("\n", "\\n")
                .replace("\r", "\\r");
    }
}
