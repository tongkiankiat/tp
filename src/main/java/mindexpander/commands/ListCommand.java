package mindexpander.commands;

import mindexpander.data.QuestionBank;
import mindexpander.data.question.Question;
import mindexpander.ui.TextUi;

import java.util.ArrayList;

/**
 * The {@code ListCommand} class updates display message
 * to show the list of questions saved in the user's device
 *
 * @author Kian Kiat
 * @version v0.2
 * @since 2025-03-11
 */
public class ListCommand extends Command {
    // Methods
    @Override
    public CommandResult execute() {
        QuestionBank questionBank = storage.load();
        return new CommandResult("", questionBank);
    }
}
