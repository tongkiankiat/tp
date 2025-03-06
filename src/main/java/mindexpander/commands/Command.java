package mindexpander.commands;

import mindexpander.exceptions.IllegalCommandException;

public class Command {
    protected String commandMessage;

    protected void updateCommandMessage(String commandMessage) {
        this.commandMessage = commandMessage;
    }

    public String execute() throws IllegalCommandException {
        return commandMessage;
    }
}
