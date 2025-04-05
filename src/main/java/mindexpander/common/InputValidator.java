package mindexpander.common;

import mindexpander.exceptions.IllegalCommandException;

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
