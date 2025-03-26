package mindexpander.tests;

import mindexpander.commands.SolveCommandOneStep;
import mindexpander.commands.CommandResult;
import mindexpander.data.QuestionBank;
import mindexpander.data.question.FillInTheBlanks;
import mindexpander.exceptions.IllegalCommandException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SolveCommandOneStepTest {
    private QuestionBank questionBank;

    @BeforeEach
    void setUp() {
        // Prepare a mock QuestionBank with some questions
        questionBank = new QuestionBank();
        questionBank.addQuestion(new FillInTheBlanks("What is 2 + 2?", "4"));
        questionBank.addQuestion(new FillInTheBlanks("What is the capital of France?", "Paris"));
    }

    @Test
    void testCorrectAnswer() {
        SolveCommandOneStep command = new SolveCommandOneStep("/q 1 /a 4", questionBank);
        CommandResult result = command.execute();
        assertEquals("Correct!", result.commandResultToUser);
    }

    @Test
    void testIncorrectAnswer() {
        SolveCommandOneStep command = new SolveCommandOneStep("/q 1 /a 5", questionBank);
        CommandResult result = command.execute();
        assertEquals("Incorrect. Please try again.", result.commandResultToUser);
    }

    @Test
    void testMissingQuestionIndex() {
        IllegalCommandException exception = assertThrows(IllegalCommandException.class, () -> {
            new SolveCommandOneStep("/q  /a 4", questionBank);
        });
        assertEquals("Question index is missing. Please provide a valid index.", exception.getMessage());
    }

    @Test
    void testMissingAnswer() {
        IllegalCommandException exception = assertThrows(IllegalCommandException.class, () -> {
            new SolveCommandOneStep("/q 1 /a ", questionBank);
        });
        assertEquals("Answer is missing. Please provide an answer.", exception.getMessage());
    }

    @Test
    void testInvalidCommandFormat() {
        IllegalCommandException exception = assertThrows(IllegalCommandException.class, () -> {
            new SolveCommandOneStep("/q 1 /", questionBank);
        });
        assertEquals("Invalid command format. Use: 'solve /q [QUESTION_INDEX] /a [ANSWER]' OR 'solve'",
            exception.getMessage());
    }

    @Test
    void testInvalidQuestionIndex() {
        SolveCommandOneStep command = new SolveCommandOneStep("/q 10 /a 4", questionBank);
        IllegalCommandException exception = assertThrows(IllegalCommandException.class, command::execute);
        assertEquals("Invalid question number. Please enter a valid index.", exception.getMessage());
    }
}
