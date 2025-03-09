package mindexpander.commands;

import mindexpander.common.Messages;

public class ExitCommand extends Command{

    public ExitCommand() {
        updateCommandMessage(Messages.GOODBYE_MESSAGE);
    }

    @Override
    public boolean keepProgramRunning() {
        return false;
    }
}
