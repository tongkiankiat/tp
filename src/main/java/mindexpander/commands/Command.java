package mindexpander.commands;

import mindexpander.exceptions.IllegalCommandException;
import mindexpander.commands.CommandResult;

public class Command {
    private String commandMessage;

    protected void updateCommandMessage(String commandMessage) {
        this.commandMessage = commandMessage;
    }

    public boolean keepProgramRunning() {
        return true;
    }

    public CommandResult execute() throws IllegalCommandException {
        return new CommandResult(commandMessage);
    }
}
