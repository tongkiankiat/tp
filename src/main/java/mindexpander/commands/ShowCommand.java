package mindexpander.commands;

import mindexpander.common.Messages;
import mindexpander.data.QuestionBank;
import mindexpander.exceptions.IllegalCommandException;

public class ShowCommand extends Command {
    // Attributes
    private final QuestionBank questionBank;
    private final int questionIndex;

    // Constructor
    public ShowCommand(QuestionBank questionBank, int questionIndex) {
        this.questionBank = questionBank;
        this.questionIndex = questionIndex;
    }

    // Methods
    private QuestionBank extractQuestion(QuestionBank questionBank, int questionIndex) {
        if (questionBank.getQuestionCount() <= 0) {
            throw new IllegalCommandException(Messages.SHOW_ERROR_EMPTY_QUESTION_BANK);
        }
        if (questionIndex < 0 || questionIndex >= questionBank.getQuestionCount()) {
            throw new IllegalCommandException(
                    Messages.showCommandOutOfRangeMessage(questionBank.getQuestionCount())
            );
        }
        QuestionBank filteredQuestionBank = new QuestionBank();
        filteredQuestionBank.addQuestion(questionBank.getQuestion(questionIndex));
        return filteredQuestionBank;
    }

    @Override
    public CommandResult execute() {
        QuestionBank filteredQuestionBank = extractQuestion(questionBank, questionIndex);
        String messageToUser = Messages.showCommandMessage(questionIndex + 1);
        return new CommandResult(messageToUser, filteredQuestionBank, true, false);
    }
}
