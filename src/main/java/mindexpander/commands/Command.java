package mindexpander.commands;

import mindexpander.exceptions.IllegalCommandException;

public class Command {
    private String commandMessage;

    protected void updateCommandMessage(String commandMessage) {
        this.commandMessage = commandMessage;
    }

    public boolean keepProgramRunning() {
        return true;
    }

    public String execute() throws IllegalCommandException {
        return commandMessage;
    }
}
