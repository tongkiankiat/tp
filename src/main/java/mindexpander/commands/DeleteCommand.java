package mindexpander.commands;

import mindexpander.data.QuestionBank;
import mindexpander.exceptions.IllegalCommandException;

/**
 * The {@code DeleteCommand} class deletes a question at a specified index from the last shown list.
 *
 * <p>This command uses the index from the last shown QuestionBank to remove a question
 * from the main QuestionBank.</p>
 *
 * Expected input: delete [index]
 *
 * @author Jensen
 * @version 1.0
 * @since 2025-03-31
 */
public class DeleteCommand extends Command {
    private final int indexToDelete;
    private final QuestionBank mainBank;
    private final QuestionBank lastShownBank;

    public DeleteCommand(int indexToDelete, QuestionBank mainBank, QuestionBank lastShownBank) {
        this.indexToDelete = indexToDelete - 1;
        this.mainBank = mainBank;
        this.lastShownBank = lastShownBank;
    }

    @Override
    public CommandResult execute() throws IllegalCommandException {
        if (indexToDelete < 0 || indexToDelete >= lastShownBank.getQuestionCount()) {
            throw new IllegalCommandException("Invalid question index.");
        }

        String deletedQuestion = lastShownBank.getQuestion(indexToDelete).toString();
        int targetIndex = mainBank.findQuestionIndex(lastShownBank.getQuestion(indexToDelete));

        if (targetIndex == -1) {
            throw new IllegalCommandException("Unable to find question in main bank to delete.");
        }

        mainBank.removeQuestion(targetIndex);
        return new CommandResult("Deleted question: " + deletedQuestion);
    }
}


