package mindexpander.commands;

import mindexpander.common.Messages;
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
    private final QuestionBank questionBank;
    private final String questionType;
    private final boolean showAnswer;

    // Constructor
    public ListCommand(QuestionBank questionBank, String questionType, Boolean showAnswer) {
        this.questionBank = questionBank;
        this.questionType = questionType;
        this.showAnswer = showAnswer;
    }

    // Methods
    private QuestionBank filteredQuestionBank(QuestionBank questionBank, String questionType, Boolean showAnswer) {
        if (questionType.equalsIgnoreCase("all")) {
            return questionBank;
        }
        QuestionBank filteredQuestionBank = new QuestionBank();
        for (int i = 0; i < questionBank.getQuestionCount(); i++) {
            boolean matchQuestionType = questionType.equals("all")
                    || questionBank.getQuestion(i).getType().getType().equalsIgnoreCase(questionType);
            if (matchQuestionType) {
                filteredQuestionBank.addQuestion(questionBank.getQuestion(i));
            }
        }
        return filteredQuestionBank;
    }

    @Override
    public CommandResult execute() {
        QuestionBank filteredQuestionBank = filteredQuestionBank(questionBank, questionType, showAnswer);
        String messageToUser = Messages.listCommandMessage(questionType, filteredQuestionBank.isEmpty());
        return new CommandResult(messageToUser, filteredQuestionBank, showAnswer, true);
    }
}
