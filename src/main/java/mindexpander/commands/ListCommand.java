package mindexpander.commands;

/**
 * The {@code ListCommand} class updates display message
 * to show the list of questions saved in the user's device
 *
 * @author Kian Kiat
 * @version v0.2
 * @since 2025-03-11
 */
public class ListCommand extends Command {
    // Attributes
    private final String LIST_COMMAND_MESSAGE = "Here are the questions you have currently: ";

    // Constructor
    public ListCommand() {
        updateCommandMessage(LIST_COMMAND_MESSAGE);
    }

    // Methods
    @Override
    public CommandResult execute() {
        // QuestionBank questionBank = storage.load();
        // return new CommandResult("", questionBank);
        // Temporary return null statement to prevent error
        return null;
    }
}
