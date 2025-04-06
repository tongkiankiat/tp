package mindexpander.commands;

import mindexpander.data.CommandHistory;

public class UndoCommand extends Command {

    public UndoCommand(CommandHistory commandHistory) {
        updateCommandMessage(commandHistory.undo());
    }
}
