package mindexpander.tests;

import mindexpander.commands.ClearCommand;
import mindexpander.commands.Command;
import mindexpander.data.CommandHistory;
import mindexpander.data.QuestionBank;
import mindexpander.data.question.FillInTheBlanks;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ClearCommandTest {
    private QuestionBank questionBank;
    private CommandHistory history;

    @BeforeEach
    public void setup() {
        questionBank = new QuestionBank();
        history = new CommandHistory();

        questionBank.addQuestion(new FillInTheBlanks("Capital of Japan is ___", "Tokyo"));
        questionBank.addQuestion(new FillInTheBlanks("2 + 2 = ?", "4"));
    }

    @Test
    public void testClearCommandExecuteThenConfirmYes() {
        QuestionBank qb = new QuestionBank();
        CommandHistory history = new CommandHistory();

        qb.addQuestion(new FillInTheBlanks("hello", "world"));

        ClearCommand clearCommand = new ClearCommand(qb, history);

        // Simulate 'y' input
        Command resultCommand = clearCommand.handleMultistepCommand("clear","y");
        assertEquals("All questions have been cleared.", resultCommand.getCommandMessage());
        assertEquals(0, qb.getQuestionCount());
    }

    @Test
    public void testClearCommandCancelWithNo() {
        QuestionBank qb = new QuestionBank();
        CommandHistory history = new CommandHistory();

        qb.addQuestion(new FillInTheBlanks("1+1", "2"));

        ClearCommand clearCommand = new ClearCommand(qb, history);

        // Simulate 'n' input
        Command resultCommand = clearCommand.handleMultistepCommand("clear","n");
        assertEquals("Clear command cancelled.", resultCommand.getCommandMessage());
        assertEquals(1, qb.getQuestionCount());
    }
}

