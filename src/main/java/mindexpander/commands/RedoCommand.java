package mindexpander.commands;

import mindexpander.data.CommandHistory;

public class RedoCommand extends Command{

    public RedoCommand(CommandHistory commandHistory) {
        updateCommandMessage(commandHistory.redo());
    }
}
