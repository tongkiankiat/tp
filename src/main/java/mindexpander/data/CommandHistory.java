//@@author Flaaaash
package mindexpander.data;

import mindexpander.commands.Traceable;
import mindexpander.exceptions.IllegalCommandException;

/**
 * Manages the history of commands for undo and redo functionality.
 * Stores a fixed-size circular buffer of {@link Traceable} commands,
 * allowing users to undo or redo actions in sequence.
 *
 * Only the latest {@value #COMMAND_HISTORY_MAX_SIZE} commands are kept in memory.
 */
public class CommandHistory {
    private static final int COMMAND_HISTORY_MAX_SIZE = 10;
    
    Traceable[] commandHistory;
    private int commandTracker;
    private int size;

    public CommandHistory() {
        commandHistory = new Traceable[COMMAND_HISTORY_MAX_SIZE];
        commandTracker = 0;
        size = 0;
    }

    /**
     * Adds a {@code Tracable} command to the history.
     * If the buffer is full, the oldest command is removed.
     *
     * @param t the command to add
     */
    public void add(Traceable t) {
        if (commandTracker < 10) {
            commandHistory[commandTracker] = t;
            commandTracker += 1;
        } else {
            for (int i = 0; i < COMMAND_HISTORY_MAX_SIZE - 1; i += 1) {
                commandHistory[i] = commandHistory[i + 1];
            }
            commandHistory[COMMAND_HISTORY_MAX_SIZE - 1] = t;
        }
        size = commandTracker;
    }

    /**
     * Undoes the last command.
     *
     * @return the undo message from the command
     * @throws IllegalCommandException if there are no commands to undo
     */
    public String undo() {
        if (commandTracker == 0) {
            throw new IllegalCommandException("Cannot undo further.");
        }
        commandTracker -= 1;
        commandHistory[commandTracker].undo();
        return commandHistory[commandTracker].undoMessage();
    }

    /**
     * Redoes the previously undone command.
     *
     * @return the redo message from the command
     * @throws IllegalCommandException if there are no commands to redo
     */
    public String redo() {
        if (commandTracker == COMMAND_HISTORY_MAX_SIZE - 1 || commandTracker == size) {
            throw new IllegalCommandException("Cannot redo further.");
        }
        commandHistory[commandTracker].redo();
        String redoMessgage = commandHistory[commandTracker].redoMessage();
        commandTracker += 1;
        return redoMessgage;
    }

}
