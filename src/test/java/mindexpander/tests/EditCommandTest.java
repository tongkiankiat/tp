package mindexpander.tests;

import mindexpander.commands.EditCommand;
import mindexpander.data.CommandHistory;
import mindexpander.data.QuestionBank;
import mindexpander.data.question.FillInTheBlanks;
import mindexpander.data.question.MultipleChoice;
import mindexpander.data.question.Question;

import java.util.List;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EditCommandTest {
    @Test
    void testInitialState() {
        QuestionBank questionBank = new QuestionBank();
        QuestionBank lastShownQuestionBank = new QuestionBank();
        CommandHistory commandHistory = new CommandHistory();
        Question question = new FillInTheBlanks("test q", "test a");
        questionBank.addQuestion(question);
        lastShownQuestionBank.addQuestion(question);
        EditCommand editCommand = new EditCommand("edit",1, "q", questionBank,
                lastShownQuestionBank, commandHistory);
        assertFalse(editCommand.isCommandComplete());
        assertEquals("Editing FITB: test q [Answer: test a] \n Please enter the new question:",
                editCommand.getCommandMessage());
    }

    @Test
    public void editCommand_correctlyEditsQuestion() {
        QuestionBank questionBank = new QuestionBank();
        QuestionBank lastShownQuestionBank = new QuestionBank();
        CommandHistory commandHistory = new CommandHistory();
        Question question = new FillInTheBlanks("test q", "test a");
        questionBank.addQuestion(question);
        lastShownQuestionBank.addQuestion(question);
        EditCommand editCommand = new EditCommand("edit",1, "q", questionBank,
                lastShownQuestionBank, commandHistory);
        editCommand.handleMultistepCommand("edit", "new q");

        assertTrue(editCommand.isCommandComplete());
        assertEquals("new q", question.getQuestion());
        assertEquals("test a", question.getAnswer());
    }

    @Test
    public void editCommand_correctlyEditsAnswer() {
        QuestionBank questionBank = new QuestionBank();
        QuestionBank lastShownQuestionBank = new QuestionBank();
        CommandHistory commandHistory = new CommandHistory();
        Question question = new FillInTheBlanks("test q", "test a");
        questionBank.addQuestion(question);
        lastShownQuestionBank.addQuestion(question);
        EditCommand editCommand = new EditCommand("edit", 1, "a", questionBank,
                lastShownQuestionBank, commandHistory);
        editCommand.handleMultistepCommand("edit","new a");

        assertTrue(editCommand.isCommandComplete());
        assertEquals("test q", question.getQuestion());
        assertEquals("new a", question.getAnswer());
    }

    @Test
    public void editCommand_correctlyEditsOption() {
        QuestionBank questionBank = new QuestionBank();
        QuestionBank lastShownQuestionBank = new QuestionBank();
        CommandHistory commandHistory = new CommandHistory();
        List<String> options = List.of("test a", "test 1", "test 2", "test 3");
        MultipleChoice question = new MultipleChoice("test q", "test a", options);
        questionBank.addQuestion(question);
        lastShownQuestionBank.addQuestion(question);
        EditCommand editCommand = new EditCommand("edit",1, "o", questionBank,
                lastShownQuestionBank, commandHistory);
        editCommand.handleMultistepCommand("edit", "1");
        editCommand.handleMultistepCommand("edit","new test");

        assertTrue(editCommand.isCommandComplete());
        assertEquals("test q", question.getQuestion());
        assertEquals("test a", question.getAnswer());
        assertTrue(question.getOptions().contains("new test"));
    }
}
