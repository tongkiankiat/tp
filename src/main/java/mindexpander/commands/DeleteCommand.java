package mindexpander.commands;

import mindexpander.data.CommandHistory;
import mindexpander.data.QuestionBank;
import mindexpander.data.question.Question;
import mindexpander.exceptions.IllegalCommandException;
import mindexpander.logging.QuestionLogger;

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
public class DeleteCommand extends Command implements Traceable {
    private final int indexToDelete;
    private int targetIndex;
    private Question deletedQuestion;
    private final QuestionBank mainBank;
    private final QuestionBank lastShownBank;
    private final CommandHistory commandHistory;

    public DeleteCommand(int indexToDelete, QuestionBank mainBank,
                         QuestionBank lastShownBank, CommandHistory commandHistory) {
        this.indexToDelete = indexToDelete - 1;
        this.mainBank = mainBank;
        this.lastShownBank = lastShownBank;
        this.commandHistory = commandHistory;
    }

    @Override
    public CommandResult execute() throws IllegalCommandException {
        assert lastShownBank != null : "lastShownBank must not be null";

        if (indexToDelete < 0 || indexToDelete >= lastShownBank.getQuestionCount()) {
            throw new IllegalCommandException("Invalid question index.");
        }

        targetIndex = mainBank.findQuestionIndex(lastShownBank.getQuestion(indexToDelete));

        if (targetIndex == -1) {
            throw new IllegalCommandException("Unable to find question in main bank to delete.");
        }

        deletedQuestion = lastShownBank.getQuestion(this.indexToDelete);
        mainBank.removeQuestion(targetIndex);
        commandHistory.add(this);
        QuestionLogger.logDeletedQuestion(deletedQuestion);
        return new CommandResult("Deleted question: " + deletedQuestion);
    }

    @Override
    public void undo() {
        mainBank.addQuestionAt(targetIndex, deletedQuestion);
    }

    public String undoMessage() {
        return String.format("%1$s successfully added.", deletedQuestion);
    }

    @Override
    public void redo() {
        mainBank.removeQuestion(targetIndex);
    }

    public String redoMessage() {
        return String.format("%1$s successfully deleted.", deletedQuestion);
    }

}
