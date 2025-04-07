package mindexpander.commands;

/**
 * An interface representing a handler for multistep commands in a user interaction flow.
 * <p>
 * Implementations of this interface manage the continuation of commands that require
 * additional user input across multiple steps.
 */
public interface Multistep {
    Command handleMultistepCommand(String userCommand, String nextInput);
}
