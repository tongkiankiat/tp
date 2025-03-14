package mindexpander.commands;

import mindexpander.data.QuestionBank;

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
    private final QuestionBank questionBank;

    // Constructor
    public ListCommand(QuestionBank questionBank) {
        this.questionBank = questionBank;
        updateCommandMessage(LIST_COMMAND_MESSAGE);
    }

    // Methods
    @Override
    public CommandResult execute() {
        return new CommandResult("", questionBank);
    }
}
