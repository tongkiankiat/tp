package mindexpander.tests;

import mindexpander.commands.CommandResult;
import mindexpander.commands.ListCommand;
import mindexpander.data.QuestionBank;
import mindexpander.data.question.FillInTheBlanks;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ListCommandTest extends DefaultTest {
    private QuestionBank questionBank;
    private ListCommand listCommand;
    private CommandResult commandResult;

    @BeforeEach
    void setup() {
        questionBank = new QuestionBank();
        questionBank.addQuestion(new FillInTheBlanks("1 + 1 = __", "2"));
        questionBank.addQuestion(new FillInTheBlanks("__ MRT Station is the closest station to NUS", "Kent Ridge"));
        listCommand = new ListCommand(questionBank);
    }

    @Test
    public void testListCommandWithNoQuestions() {
        questionBank = new QuestionBank();
        listCommand = new ListCommand(questionBank);
        commandResult = listCommand.execute();
        assertEquals("You have no questions yet!", commandResult.commandResultToUser);
    }

    @Test
    public void testListCommandWithQuestions() {
        setup();
        commandResult = listCommand.execute();
        assertEquals("Here are the questions you have stored:", commandResult.commandResultToUser);
        ArrayList<String> questionBankStringArray = new ArrayList<>();
        for (int i = 0; i < questionBank.getQuestionCount(); i++){
            questionBankStringArray.add((i + 1) + ". " + questionBank.getQuestion(i).toString());
        }
        assertEquals(List.of(
                "1. FITB: 1 + 1 = __ [Answer: 2]",
                "2. FITB: __ MRT Station is the closest station to NUS [Answer: Kent Ridge]"
        ), questionBankStringArray);
    }
}
