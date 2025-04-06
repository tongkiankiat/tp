package mindexpander.data;

import mindexpander.commands.Tracable;
import mindexpander.exceptions.IllegalCommandException;

public class CommandHistory {
    Tracable[] commandHistory;
    private int commandTracker;
    private int size;
    private final int COMMAND_HISTORY_MAX_SIZE = 10;

    public CommandHistory() {
        commandHistory = new Tracable[COMMAND_HISTORY_MAX_SIZE];
        commandTracker = 0;
        size = 0;
    }

    public void add(Tracable t) {
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

    public String undo() {
        if (commandTracker == 0) {
            throw new IllegalCommandException("Cannot undo further.");
        }
        commandTracker -= 1;
        commandHistory[commandTracker].undo();
        return commandHistory[commandTracker].undoMessage();
    }

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
