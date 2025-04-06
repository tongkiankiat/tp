package mindexpander.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import mindexpander.common.InputValidator;
import mindexpander.common.Messages;
import mindexpander.exceptions.IllegalCommandException;

import org.junit.jupiter.api.Test;

public class InputValidatorTest {

    @Test
    void testValidateInput_rejectsReservedDelimiter() {
        String badInput = "This contains " + Messages.STORAGE_DELIMITER;
        IllegalCommandException exception = assertThrows(IllegalCommandException.class,
                () -> InputValidator.validateInput(badInput));
        assertEquals("Input cannot contain the reserved delimiter string.", exception.getMessage());
    }

    @Test
    void testFITBQuestion_withDelimiter_throwsException() {
        String fitbInput = "The answer is " + Messages.STORAGE_DELIMITER;
        assertThrows(IllegalCommandException.class, () -> InputValidator.validateInput(fitbInput));
    }

    @Test
    void testMCQOption_withDelimiter_throwsException() {
        String mcqOption = "Choice A: Apple " + Messages.STORAGE_DELIMITER;
        assertThrows(IllegalCommandException.class, () -> InputValidator.validateInput(mcqOption));
    }

    @Test
    void testTFAnswer_withDelimiter_throwsException() {
        String tfAnswer = "true" + Messages.STORAGE_DELIMITER;
        assertThrows(IllegalCommandException.class, () -> InputValidator.validateInput(tfAnswer));
    }

    @Test
    void testValidInput_passesValidation() {
        String goodInput = "This is a valid input with no delimiter.";
        InputValidator.validateInput(goodInput); // Should not throw
    }
}
