package mindexpander.exceptions;

/**
 * Exception thrown when a user enters an invalid command.
 * This exception is used to indicate that the command does not match any known actions.
 */
public class InvalidCommandException extends Exception {

    /**
     * Constructs an {@code InvalidCommandException} with a specific error message.
     *
     * @param message The error message describing the issue.
     */
    public InvalidCommandException(String message) {
        super(message);
    }
}

