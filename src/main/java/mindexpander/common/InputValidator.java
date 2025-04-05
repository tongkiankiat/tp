package mindexpander.common;

import mindexpander.exceptions.IllegalCommandException;

/**
 * The {@code InputValidator} class provides utility methods for validating user inputs
 * to prevent data corruption during saving or loading of questions.
 *
 * <p>Specifically, it checks for reserved delimiter strings used internally for file storage
 * to ensure user input does not break the structured save/load format.</p>
 */
public class InputValidator {
    private static final String DELIMITER = Messages.STORAGE_DELIMITER;

    /**
     * Validates input for forbidden characters like the custom delimiter.
     *
     * @param input The user input to validate.
     * @throws IllegalCommandException If input is invalid.
     */
    public static void validateInput(String input) throws IllegalCommandException {
        // Check for reserved delimiter string
        if (input.contains(DELIMITER)) {
            throw new IllegalCommandException("Input cannot contain the reserved delimiter string.");
        }
    }
}
