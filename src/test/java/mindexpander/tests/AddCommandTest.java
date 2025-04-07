package mindexpander.tests;

import mindexpander.commands.AddCommand;
import mindexpander.data.CommandHistory;
import mindexpander.data.QuestionBank;
import mindexpander.data.question.Question;
import mindexpander.data.question.QuestionType;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AddCommandTest {
    @Test
    void testInitialState() {
        QuestionBank questionBank = new QuestionBank();
        CommandHistory commandHistory = new CommandHistory();
        AddCommand addCommand = new AddCommand(questionBank, commandHistory);
        assertFalse(addCommand.isCommandComplete());
        assertEquals("Please enter the type of the question you would like to add. (fitb/mcq/tf)",
                addCommand.getCommandMessage());
    }

    @Test
    public void addCommand_invalidType() {
        QuestionBank questionBank = new QuestionBank();
        CommandHistory commandHistory = new CommandHistory();
        AddCommand addCommand = new AddCommand(questionBank, commandHistory);
        final String[] invalidTypes = {"abc", "[]\\[;]", "question"};
        for (String type : invalidTypes) {
            addCommand.handleMultistepCommand(type, type);
            assertEquals("Invalid input. Please enter a correct question type. (fitb/mcq/tf)",
                    addCommand.getCommandMessage());
        }
    }

    @Test
    public void addCommand_emptyInput() {
        QuestionBank questionBank = new QuestionBank();
        CommandHistory commandHistory = new CommandHistory();
        AddCommand addCommand = new AddCommand(questionBank, commandHistory);
        addCommand.handleMultistepCommand("add", "fitb");
        addCommand.handleMultistepCommand("add","");
        assertEquals("Input cannot be empty!", addCommand.getCommandMessage());
    }

    @Test
    public void addCommand_validData_correctlyConstructed() {
        QuestionBank questionBank = new QuestionBank();
        CommandHistory commandHistory = new CommandHistory();
        AddCommand addCommand = new AddCommand(questionBank, commandHistory);
        addCommand.handleMultistepCommand("add","fitb");
        addCommand.handleMultistepCommand("add","What is 2 + 2?");
        addCommand.handleMultistepCommand("add","4");
        Question question = questionBank.getQuestion(0);

        assertTrue(addCommand.isCommandComplete());
        assertEquals(QuestionType.FITB, question.getType());
        assertEquals("What is 2 + 2?", question.getQuestion());
        assertEquals("4", question.getAnswer());
    }
}
