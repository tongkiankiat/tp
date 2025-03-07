package mindexpander.commands;

import java.util.List;

import mindexpander.data.QuestionBank;

public class ListQuestions extends Command {
    StringBuilder commandMessage = new StringBuilder();

    public ListQuestions(QuestionBank questionBank) {
        List<String> questionList = questionBank.list();
        for (String question : questionList) {
            commandMessage.append(question).append("\n");
        }
        updateCommandMessage(commandMessage.toString().trim());
    }
}
