package mindexpander.tests;

import mindexpander.commands.HelpCommand;
import mindexpander.exceptions.IllegalCommandException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Unit test for {@code HelpCommand} class.
 *
 * @author Wenyi
 */
public class HelpCommandTest {


    /*
     * NOTE for future devs: update the HELP_MESSAGE string with
     * whatever new features are implemented.
     */
    private static final String DEFAULT_HELP_MESSAGE = """
            Welcome to MindExpander!
            
            List of commands:
            1. help
                - Print a list of commands.
            2. add
                - Add a question to the list.
            3. list
                - List all questions added.
            4. solve
                - Attempt to solve a question.
            5. edit
                - Edit a question in the list.
            6. find
                - Find a question in the list that contains a specific keyword.
            7. delete
                - Delete a question in the list.
            8. exit
                - Exit the program.
            To get detailed information on a specific command, use `help [COMMAND]`, e.g. `help add`.""";

    private static final String HELP_HELP_MESSAGE = """
            *The `help` command*
            
            Print a full list of commands or append a specific command to get detailed information on it.
            The command can be used in two ways, shown below.
            
            Usage:
             - `help`: print the list of commands.
             - `help [COMMAND]`: print the detailed information of a specific command.
            
            Example:
             - `help`
             - `help add`""";

    private static final String ADD_HELP_MESSAGE = """
            *The `add` command*
            
            Adds a question to the question bank by following a series of steps.
            Possible question types (abbreviation in the brackets [] are how the type should be entered): 
            Fill in the Blanks - [FITB]
            Multiple Choice Question - [MCQ]
            True or False Question - [TF]
            
            Usage:
             - Follow the steps below:
             1. `add`
             2. [QUESTION_TYPE]
             3. [QUESTION_DETAILS]
             4. [QUESTION_ANSWER]
            
            Example:
            - To add a FITB question, "What is 1 + 1?", with the answer "2".
             1. add
             2. FITB
             3. What is 1+1?
             4. 2""";

    private static final String LIST_HELP_MESSAGE = """
            *The `list` command*
            
            Displays all the questions currently stored in the question bank.
            
            Usage:
             - 'list': print the list of questions currently stored in the question bank without answers.
             - 'list answer': print the list of questions currently stored in the question bank with answers.""";

    private static final String SOLVE_HELP_MESSAGE = """
            *The `solve` command*
            
            Allows the user to attempt answering a stored question in the question bank.
            The command can be used in two ways, shown below.
            
            Usage:
               1. 'solve [QUESTION_INDEX]'
               2. '[ANSWER]'
            
            Example, for "Q1. Are the developers of MindExpander cool?":
            - Multi-step:
             1. 'solve 1'
             2. 'yes'""";

    private static final String EDIT_HELP_MESSAGE = """
            *The `edit` command*
            
            Edits a question's details, including question content, answer and for multiple choice questions, their
            incorrect options.
            
            Usage:
             - 'edit [QUESTION_INDEX] q': edit the question content of a question.
             - 'edit [QUESTION_INDEX] a': edit the answer of a question.
             - 'edit [QUESTION_INDEX] o': edit the incorrect options of a multiple choice question.
            Example:
             - 'edit 1 q'
             - 'what is 3 - 2?'""";

    // Haha, "find help", get it?
    private static final String FIND_HELP_MESSAGE = """
            *The `find` command*
            
            Allows the user to find a question previously added to the question bank.
            The command can be used in two ways, shown below.
            
            Usage:
             - 'find [QUESTION_DETAILS]': search for all question types with details matching [QUESTION_DETAILS].
             - 'find mcq [QUESTION_DETAILS]': search for mcq questions with details matching [QUESTION_DETAILS].
             - 'find fitb [QUESTION_DETAILS]': search for fitb questions with details matching [QUESTION_DETAILS].
            
            Example:
            - 'find hello'
            - 'find mcq hello'
            - 'find fitb hello'""";

    private static final String DELETE_HELP_MESSAGE = """
            *The `delete` command*
            
            Deletes a question from the question bank.
            
            Usage:
             - 'delete [QUESTION_INDEX]': Delete the question at [QUESTION_INDEX].
            
            Example:
            - 'delete 1'""";

    private static final String EXIT_HELP_MESSAGE = """
            *The `exit` command*
            
            Exits the program.
            
            Usage:
            - 'exit': exit the program.""";

    @Test
    public void testHelpCommandMessage() {
        HelpCommand helpCommand = new HelpCommand("");

        assertEquals(DEFAULT_HELP_MESSAGE, helpCommand.getCommandMessage(),
            "Help message does not match expected output.");
    }

    @Test
    public void testHelpHelpCommandMessage() {
        HelpCommand helpCommand = new HelpCommand("help");

        assertEquals(HELP_HELP_MESSAGE, helpCommand.getCommandMessage(),
                "Help message does not match expected output.");
    }

    @Test
    public void testAddHelpCommandMessage() {
        HelpCommand helpCommand = new HelpCommand("add");

        assertEquals(ADD_HELP_MESSAGE, helpCommand.getCommandMessage(),
                "Help message does not match expected output.");
    }

    @Test
    public void testListHelpCommandMessage() {
        HelpCommand helpCommand = new HelpCommand("list");

        assertEquals(LIST_HELP_MESSAGE, helpCommand.getCommandMessage(),
                "Help message does not match expected output.");
    }

    @Test
    public void testSolveHelpCommandMessage() {
        HelpCommand helpCommand = new HelpCommand("solve");

        assertEquals(SOLVE_HELP_MESSAGE, helpCommand.getCommandMessage(),
                "Help message does not match expected output.");
    }

    @Test
    public void testFindHelpCommandMessage() {
        HelpCommand helpCommand = new HelpCommand("find");

        assertEquals(FIND_HELP_MESSAGE, helpCommand.getCommandMessage(),
                "Help message does not match expected output.");
    }

    @Test
    public void testExitHelpCommandMessage() {
        HelpCommand helpCommand = new HelpCommand("exit");

        assertEquals(EXIT_HELP_MESSAGE, helpCommand.getCommandMessage(),
                "Help message does not match expected output.");
    }

    @Test
    public void testDeleteHelpCommandMessage() {
        HelpCommand helpCommand = new HelpCommand("delete");

        assertEquals(DELETE_HELP_MESSAGE, helpCommand.getCommandMessage(),
                "Help message does not match expected output.");
    }

    @Test
    public void testInvalidCommandThrowsException() {
        String invalidCommand = "bogus_command";

        IllegalCommandException exception = assertThrows(
                IllegalCommandException.class,
                () -> new HelpCommand(invalidCommand),
                "Expected an IllegalCommandException for an unknown command."
        );

        assertTrue(exception.getMessage().contains("No help available"),
                "Error message should indicate no help is available for unknown commands.");
    }

}
