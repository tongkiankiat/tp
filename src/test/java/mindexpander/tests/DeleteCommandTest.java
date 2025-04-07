package mindexpander.tests;

import mindexpander.commands.CommandResult;
import mindexpander.commands.DeleteCommand;
import mindexpander.data.CommandHistory;
import mindexpander.data.QuestionBank;
import mindexpander.data.question.FillInTheBlanks;
import mindexpander.exceptions.IllegalCommandException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DeleteCommandTest extends DefaultTest {
    private QuestionBank mainBank;
    private QuestionBank lastShownBank;
    private CommandHistory history;

    @BeforeEach
    public void setup() {
        mainBank = new QuestionBank();
        // For testing, use the same instance for mainBank and lastShownBank.
        lastShownBank = mainBank;
        mainBank.addQuestion(new FillInTheBlanks("What is 1+1?", "2"));
        mainBank.addQuestion(new FillInTheBlanks("Capital of France is __", "Paris"));
    }

    @Test
    public void testDeleteCommand_validIndex() {
        DeleteCommand.enableDelete();

        // Store the expected deleted question's string (from the last shown list at index 0)
        String expectedDeleted = lastShownBank.getQuestion(0).toString();
        CommandHistory commandHistory = new CommandHistory();
        // Create delete command for the first question (user input 'delete 1')
        DeleteCommand deleteCommand = new DeleteCommand(1, mainBank, lastShownBank, commandHistory);
        CommandResult result = deleteCommand.execute();
        // Verify the returned message is correct
        assertEquals("Deleted question: " + expectedDeleted, result.commandResultToUser);
        // Check that the main bank now contains one question
        assertEquals(1, mainBank.getQuestionCount());
    }

    @Test
    public void testDeleteCommand_zeroIndex() {
        DeleteCommand.enableDelete();

        DeleteCommand deleteCommand = new DeleteCommand(0, mainBank, lastShownBank, history);
        IllegalCommandException exception = assertThrows(IllegalCommandException.class,
                deleteCommand::execute);
        assertEquals("Invalid question index.", exception.getMessage());
    }

    @Test
    public void testDeleteCommand_invalidIndex() {
        DeleteCommand.enableDelete();

        // Try deleting with an out-of-bounds index (e.g., delete 3 when there are only 2 questions)
        CommandHistory commandHistory = new CommandHistory();
        DeleteCommand deleteCommand = new DeleteCommand(3, mainBank, lastShownBank, commandHistory);
        IllegalCommandException exception = assertThrows(IllegalCommandException.class,
                () -> deleteCommand.execute());
        assertEquals("Invalid question index.", exception.getMessage());
    }

    @Test
    public void testDeleteCommand_disabledAfterOneUse() {
        CommandHistory commandHistory = new CommandHistory();

        // First delete should succeed
        DeleteCommand deleteCommand1 = new DeleteCommand(1, mainBank, lastShownBank, commandHistory);
        deleteCommand1.execute();

        // Second delete should throw error since delete is disabled
        DeleteCommand deleteCommand2 = new DeleteCommand(1, mainBank, lastShownBank, commandHistory);
        IllegalCommandException exception = assertThrows(IllegalCommandException.class,
                deleteCommand2::execute);
        assertEquals("Please run 'list' or 'find' to get an updated list before using delete.", exception.getMessage());
    }

    @Test
    public void testDeleteCommand_reEnabledAfterList() {
        DeleteCommand.enableDelete();

        CommandHistory commandHistory = new CommandHistory();

        // First delete
        DeleteCommand deleteCommand1 = new DeleteCommand(1, mainBank, lastShownBank, commandHistory);
        deleteCommand1.execute();

        // Simulate list/find re-enabling delete
        DeleteCommand.enableDelete();

        // Second delete should now work again
        DeleteCommand deleteCommand2 = new DeleteCommand(1, mainBank, lastShownBank, commandHistory);
        deleteCommand2.execute();

        assertEquals(0, mainBank.getQuestionCount());
    }

}
