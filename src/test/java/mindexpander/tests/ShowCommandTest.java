package mindexpander.tests;

import mindexpander.commands.CommandResult;
import mindexpander.commands.ShowCommand;
import mindexpander.data.QuestionBank;
import mindexpander.data.question.FillInTheBlanks;
import mindexpander.data.question.MultipleChoice;
import mindexpander.data.question.TrueFalse;
import mindexpander.exceptions.IllegalCommandException;
import mindexpander.parser.Parser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ShowCommandTest extends DefaultTest {

    private QuestionBank questionBank;
    private ShowCommand showCommand;
    private CommandResult commandResult;

    @BeforeEach
    void setup() {
        List<String> options = new ArrayList<>();
        options.add("1");
        options.add("2");
        options.add("3");
        questionBank = new QuestionBank();
        questionBank.addQuestion(new FillInTheBlanks("Which MRT Station is the closest station to NUS?", "Kent Ridge"));
        questionBank.addQuestion(new MultipleChoice("20 * 30?", "600", options));
        questionBank.addQuestion(new TrueFalse("Is CS2113 a fun mod?", "true"));
    }

    @Test
    void testShowCommandOutOfRange() {
        ShowCommand showCommand = new ShowCommand(questionBank, -1);
        IllegalCommandException thrown = assertThrows(IllegalCommandException.class, () -> showCommand.execute());
        assertEquals("Index is out of range! You currently have " +
                questionBank.getQuestionCount() +
                " questions, please enter a question index in this range.", thrown.getMessage());
    }

    @Test
    void testShowCommandOutOfRangeParser() {
        String userInput = "show -1";
        IllegalCommandException thrown = assertThrows(
                IllegalCommandException.class,
                () -> new Parser().parseCommand(userInput, questionBank, questionBank)
        );
        assertEquals("Index is out of range! You currently have " +
                questionBank.getQuestionCount() +
                " questions, please enter a question index in this range.", thrown.getMessage());
    }

    @Test
    void testShowCommandEmptyQuestionBank() {
        questionBank = new QuestionBank();
        showCommand = new ShowCommand(questionBank, 0);
        IllegalCommandException thrown = assertThrows(IllegalCommandException.class, () -> showCommand.execute());
        assertEquals("Invalid command! There are currently no questions in the question bank.",
                thrown.getMessage());
    }

    @Test
    void testShowCommand() {
        showCommand = new ShowCommand(questionBank, 2);
        commandResult = showCommand.execute();
        assertEquals("Here is the answer for question 3:", commandResult.commandResultToUser);
        assertEquals("TF: Is CS2113 a fun mod? [Answer: true]",
                commandResult.getQuestionBank().getQuestion(0).toString());
    }
}
