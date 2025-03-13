package mindexpander.commands;

import mindexpander.data.QuestionBank;

public class CommandResult {
    public final String commandResultToUser;
    public final QuestionBank questionBank;

    public CommandResult(String commandResultToUser, QuestionBank questionBank) {
        this.commandResultToUser = commandResultToUser;
        this.questionBank = questionBank;
    }

    public CommandResult(String commandResultToUser) {
        this.commandResultToUser = commandResultToUser;
        this.questionBank = null;
    }

    public QuestionBank getQuestionBank() {
        return questionBank;
    }
}
