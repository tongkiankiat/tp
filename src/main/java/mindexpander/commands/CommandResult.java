package mindexpander.commands;

import mindexpander.data.QuestionBank;

public class CommandResult {
    public final String commandResultToUser;
    public final QuestionBank questionBank;
    public final boolean showAnswer;
    public boolean updateLastShownQuestionBank = true;

    public CommandResult(
            String commandResultToUser,
            QuestionBank questionBank,
            Boolean showAnswer,
            Boolean updateLastShownQuestionBank
    ) {
        this.commandResultToUser = commandResultToUser;
        this.questionBank = questionBank;
        this.showAnswer = showAnswer;
        this.updateLastShownQuestionBank = updateLastShownQuestionBank;
    }

    public CommandResult(String commandResultToUser) {
        this.commandResultToUser = commandResultToUser;
        this.questionBank = new QuestionBank();
        this.showAnswer = false;
    }

    public QuestionBank getQuestionBank() {
        return questionBank;
    }
}
